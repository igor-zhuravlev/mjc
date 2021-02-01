package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import com.epam.esm.service.exception.validation.GiftCertificateNotValidException;
import com.epam.esm.service.util.ParamsUtil;
import com.epam.esm.service.validation.ParamsValidator;
import com.epam.esm.service.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.anyMap;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;
    @Mock
    private Validator<GiftCertificateDto> giftCertificateDtoValidator;
    @Mock
    private ParamsValidator paramsValidator;

    @InjectMocks
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl();

    private static List<GiftCertificate> giftCertificateList;
    private static List<GiftCertificateDto> giftCertificateDtoList;

    @BeforeAll
    static void beforeAll() {
        GiftCertificate giftCertificate1 = new GiftCertificate();
        giftCertificate1.setId(1L);
        giftCertificate1.setDescription("description1");
        giftCertificate1.setPrice(BigDecimal.valueOf(9.99));
        giftCertificate1.setDuration(10);

        GiftCertificate giftCertificate2 = new GiftCertificate();
        giftCertificate2.setId(2L);
        giftCertificate2.setDescription("description2");
        giftCertificate2.setPrice(BigDecimal.valueOf(9.99));
        giftCertificate2.setDuration(10);

        giftCertificateList = new ArrayList<>();
        giftCertificateList.add(giftCertificate1);
        giftCertificateList.add(giftCertificate2);

        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto();
        giftCertificateDto1.setDescription("description");
        giftCertificateDto1.setPrice(BigDecimal.valueOf(9.99));
        giftCertificateDto1.setDuration(10);

        GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setDescription("description");
        giftCertificateDto2.setPrice(BigDecimal.valueOf(9.99));
        giftCertificateDto2.setDuration(10);

        giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(giftCertificateDto1);
        giftCertificateDtoList.add(giftCertificateDto2);
    }

    @Test
    void findAll_FoundAllCertificates_ReturnListOfCertificates() {
        Map<String, String[]> params = new HashMap<>();
        params.put("tag", new String[]{"someTag"});

        Criteria criteria = ParamsUtil.buildCriteria(params);

        given(paramsValidator.isValid(params)).willReturn(true);
        given(giftCertificateRepository.findAll(criteria)).willReturn(giftCertificateList);
        given(giftCertificateConverter.entityToDtoList(giftCertificateList)).willReturn(giftCertificateDtoList);

        List<GiftCertificateDto> actual = giftCertificateService.findAll(params);

        assertEquals(giftCertificateDtoList, actual);

        then(paramsValidator)
                .should(only())
                .isValid(anyMap());

        then(giftCertificateRepository)
                .should(only())
                .findAll(any(Criteria.class));

        then(giftCertificateConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_CertificateExist_ReturnFoundCertificate() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        final Long id = giftCertificate.getId();

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateRepository.find(criteria)).willReturn(giftCertificate);
        given(giftCertificateConverter.entityToDto(giftCertificate)).willReturn(giftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.findById(id);

        assertNotNull(actual);
        assertEquals(actual, giftCertificateDto);

        then(giftCertificateRepository)
                .should(only())
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(only())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void findById_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateRepository.find(criteria)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.findById(id);
        });

        then(giftCertificateRepository)
                .should(only())
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void save_SavingGiftCertificate_ReturnSavedGiftCertificate() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        GiftCertificate saved = giftCertificate;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, giftCertificate.getName());

        given(giftCertificateDtoValidator.isValidToSave(giftCertificateDto)).willReturn(true);
        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.find(criteria)).willReturn(null);
        given(giftCertificateRepository.save(giftCertificate)).willReturn(saved);
        given(giftCertificateConverter.entityToDto(saved)).willReturn(giftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.save(giftCertificateDto);

        assertEquals(giftCertificateDto, actual);

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToSave(any(GiftCertificateDto.class));

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(times(1))
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(times(1))
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void save_CertificateExist_GiftCertificateAlreadyExistExceptionThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        GiftCertificate existed = giftCertificate;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, giftCertificate.getName());

        given(giftCertificateDtoValidator.isValidToSave(giftCertificateDto)).willReturn(true);
        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.find(criteria)).willReturn(existed);

        assertThrows(GiftCertificateAlreadyExistException.class, () -> {
            giftCertificateService.save(giftCertificateDto);
        });

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToSave(any(GiftCertificateDto.class));

        then(giftCertificateConverter)
                .should(only())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(only())
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(never())
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void save_InvalidCertificate_GiftCertificateNotValidExceptionThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);

        given(giftCertificateDtoValidator.isValidToSave(giftCertificateDto)).willReturn(false);

        assertThrows(GiftCertificateNotValidException.class, () -> {
            giftCertificateService.save(giftCertificateDto);
        });

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToSave(any(GiftCertificateDto.class));

        then(giftCertificateConverter)
                .should(never())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(never())
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateById_UpdatingCertificate_ReturnUpdatedCertificate() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        GiftCertificate updated = giftCertificate;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateDtoValidator.isValidToUpdate(giftCertificateDto)).willReturn(true);
        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.update(giftCertificate)).willReturn(1L);
        given(giftCertificateRepository.find(criteria)).willReturn(updated);
        given(giftCertificateConverter.entityToDto(updated)).willReturn(giftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.updateById(id, giftCertificateDto);

        assertEquals(giftCertificateDto, actual);

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToUpdate(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(2))
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(times(1))
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateById_CertificateNotUpdated_UnableUpdateGiftCertificateThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateDtoValidator.isValidToUpdate(giftCertificateDto)).willReturn(true);
        given(giftCertificateRepository.find(criteria)).willReturn(giftCertificate);
        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.update(giftCertificate)).willReturn(0L);

        assertThrows(UnableUpdateGiftCertificate.class, () -> {
            giftCertificateService.updateById(id, giftCertificateDto);
        });

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToUpdate(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateById_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateDtoValidator.isValidToUpdate(giftCertificateDto)).willReturn(true);
        given(giftCertificateRepository.find(criteria)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.updateById(id, giftCertificateDto);
        });

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToUpdate(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(only())
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(never())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateById_InvalidCertificate_GiftCertificateNotValidExceptionThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        final Long id = 1L;

        given(giftCertificateDtoValidator.isValidToUpdate(giftCertificateDto)).willReturn(false);

        assertThrows(GiftCertificateNotValidException.class, () -> {
            giftCertificateService.updateById(id, giftCertificateDto);
        });

        then(giftCertificateDtoValidator)
                .should(only())
                .isValidToUpdate(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .find(any(Criteria.class));

        then(giftCertificateConverter)
                .should(never())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void deleteById_DeleteCertificate_ReturnNothing() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateRepository.find(criteria)).willReturn(giftCertificate);
        given(giftCertificateRepository.deleteById(id)).willReturn(1L);

        giftCertificateService.deleteById(id);

        then(giftCertificateRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(times(1))
                .deleteById(anyLong());
    }

    @Test
    void deleteById_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateRepository.find(criteria)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.findById(id);
        });

        then(giftCertificateRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(never())
                .deleteById(anyLong());
    }

    @Test
    void deleteById_CertificateNoDeleted_UnableDeleteGiftCertificateExceptionThrown() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(giftCertificateRepository.find(criteria)).willReturn(giftCertificate);
        given(giftCertificateRepository.deleteById(id)).willReturn(0L);

        assertThrows(UnableDeleteGiftCertificateException.class, () -> {
            giftCertificateService.deleteById(id);
        });

        then(giftCertificateRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(giftCertificateRepository)
                .should(times(1))
                .deleteById(anyLong());
    }
}