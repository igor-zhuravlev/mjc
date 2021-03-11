package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.OrderGiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;
    @Mock
    private Converter<GiftCertificate, GiftCertificateUpdateDto> giftCertificateUpdateConverter;

    @InjectMocks
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl();

    private static List<GiftCertificate> giftCertificateList;
    private static List<GiftCertificateDto> giftCertificateDtoList;

    @BeforeAll
    static void beforeAll() {
        giftCertificateList = new ArrayList<>();
        giftCertificateDtoList = new ArrayList<>();

        for (long i = 1; i <= 2; i++) {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(i);
            giftCertificate.setName("name" + i);

            GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
            giftCertificateDto.setId(i);
            giftCertificateDto.setName("name" + i);

            giftCertificateList.add(giftCertificate);
            giftCertificateDtoList.add(giftCertificateDto);
        }
    }

    @Test
    void findAll_FoundAllCertificates_ReturnListOfCertificates() {
        PageDto pageDto = new PageDto(5, 1);
        GiftCertificateParamDto giftCertificateParamDto = new GiftCertificateParamDto();
        GiftCertificateCriteria giftCertificateCriteria = new GiftCertificateCriteria();

        given(giftCertificateRepository
                .findAll(giftCertificateCriteria, pageDto.getOffset(), pageDto.getLimit()))
                .willReturn(giftCertificateList);
        given(giftCertificateConverter.entityToDtoList(giftCertificateList))
                .willReturn(giftCertificateDtoList);

        List<GiftCertificateDto> actual = giftCertificateService
                .findAll(giftCertificateParamDto, pageDto);

        assertNotNull(actual);
        assertEquals(giftCertificateDtoList, actual);

        then(giftCertificateRepository)
                .should(only())
                .findAll(any(GiftCertificateCriteria.class), anyInt(), anyInt());

        then(giftCertificateConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_CertificateExist_ReturnFoundCertificate() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        final Long id = giftCertificate.getId();

        given(giftCertificateRepository.findById(id))
                .willReturn(giftCertificate);
        given(giftCertificateConverter.entityToDto(giftCertificate))
                .willReturn(giftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.findById(id);

        assertNotNull(actual);
        assertEquals(actual, giftCertificateDto);

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());

        then(giftCertificateConverter)
                .should(only())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void findById_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        final Long id = 1L;

        given(giftCertificateRepository.findById(id)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.findById(id);
        });

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void create_CreatingGiftCertificate_ReturnCreatedGiftCertificate() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        final String name = giftCertificate.getName();
        GiftCertificate createdGiftCertificate = giftCertificate;
        GiftCertificateDto createdGiftCertificateDto = giftCertificateDto;

        given(giftCertificateConverter.dtoToEntity(giftCertificateDto))
                .willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(name))
                .willReturn(null);
        given(giftCertificateRepository.save(giftCertificate))
                .willReturn(createdGiftCertificate);
        given(giftCertificateConverter.entityToDto(createdGiftCertificate))
                .willReturn(createdGiftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.create(giftCertificateDto);

        assertNotNull(actual);
        assertEquals(createdGiftCertificateDto, actual);

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(name);

        then(giftCertificateRepository)
                .should(times(1))
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(times(1))
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void create_CertificateExist_GiftCertificateAlreadyExistExceptionThrown() {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        final String name = giftCertificate.getName();
        GiftCertificate existedGiftCertificate = giftCertificate;

        given(giftCertificateConverter.dtoToEntity(giftCertificateDto))
                .willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(name))
                .willReturn(existedGiftCertificate);

        assertThrows(GiftCertificateAlreadyExistException.class, () -> {
            giftCertificateService.create(giftCertificateDto);
        });

        then(giftCertificateConverter)
                .should(only())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(only())
                .findByName(name);

        then(giftCertificateRepository)
                .should(never())
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void update_UpdatingCertificate_ReturnUpdatedCertificate() {
        GiftCertificateUpdateDto giftCertificateUpdateDto = new GiftCertificateUpdateDto();
        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        final Long id = giftCertificate.getId();
        GiftCertificate existedGiftCertificate = giftCertificate;
        GiftCertificate updatedGiftCertificate = giftCertificate;
        GiftCertificateDto updatedGiftCertificateDto = giftCertificateDto;

        given(giftCertificateRepository.findById(id))
                .willReturn(existedGiftCertificate);
        given(giftCertificateUpdateConverter.dtoToEntity(giftCertificateUpdateDto))
                .willReturn(giftCertificate);
        given(giftCertificateRepository.update(giftCertificate))
                .willReturn(updatedGiftCertificate);
        given(giftCertificateConverter.entityToDto(updatedGiftCertificate))
                .willReturn(updatedGiftCertificateDto);

        GiftCertificateDto actual = giftCertificateService.update(id, giftCertificateUpdateDto);

        assertNotNull(actual);
        assertEquals(updatedGiftCertificateDto, actual);

        then(giftCertificateRepository)
                .should(times(1))
                .findById(anyLong());

        then(giftCertificateUpdateConverter)
                .should(only())
                .dtoToEntity(any(GiftCertificateUpdateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(only())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void update_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        GiftCertificateUpdateDto giftCertificateUpdateDto = new GiftCertificateUpdateDto();
        final Long id = 1L;

        given(giftCertificateRepository.findById(id)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.update(id, giftCertificateUpdateDto);
        });

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());

        then(giftCertificateUpdateConverter)
                .should(never())
                .dtoToEntity(any(GiftCertificateUpdateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .update(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void delete_DeleteCertificate_ReturnNothing() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        giftCertificate.setOrderGiftCertificates(new HashSet<>());

        given(giftCertificateRepository.findById(id))
                .willReturn(giftCertificate);
        willDoNothing().given(giftCertificateRepository)
                .delete(giftCertificate);

        giftCertificateService.delete(id);

        then(giftCertificateRepository)
                .should(times(1))
                .findById(anyLong());

        then(giftCertificateRepository)
                .should(times(1))
                .delete(any(GiftCertificate.class));
    }

    @Test
    void delete_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
        final Long id = 1L;

        given(giftCertificateRepository.findById(id)).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.delete(id);
        });

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());

        then(giftCertificateRepository)
                .should(never())
                .delete(any(GiftCertificate.class));
    }

    @Test
    void delete_GiftCertificateUsedByOrder_UnableDeleteGiftCertificateExceptionThrown() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        final Long id = giftCertificate.getId();

        giftCertificate.setOrderGiftCertificates(Set.of(new OrderGiftCertificate()));

        given(giftCertificateRepository.findById(id))
                .willReturn(giftCertificate);

        assertThrows(UnableDeleteGiftCertificateException.class, () -> {
            giftCertificateService.delete(id);
        });

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());

        then(giftCertificateRepository)
                .should(never())
                .delete(any(GiftCertificate.class));
    }
}