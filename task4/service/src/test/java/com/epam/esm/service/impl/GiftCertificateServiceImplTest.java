package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.OrderGiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ResourceAlreadyExistException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.UnableDeleteResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl();

    @Test
    void findById_CertificateExist_ReturnFoundCertificate() {
        final Long id = 1L;

        GiftCertificate giftCertificate = new GiftCertificate();
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();

        given(giftCertificateRepository.findById(id))
                .willReturn(Optional.of(giftCertificate));
        given(modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .willReturn(giftCertificateDto);

        giftCertificateService.findById(id);

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(only())
                .map(any(GiftCertificate.class), eq(GiftCertificateDto.class));
    }

    @Test
    void findById_CertificateNotFound_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(giftCertificateRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftCertificateService.findById(id);
        });

        then(giftCertificateRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(never())
                .map(any(GiftCertificate.class), eq(GiftCertificateDto.class));
    }

    @Test
    void create_CreatingGiftCertificate_ReturnCreatedGiftCertificate() {
        final String name = "gift1";

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName(name);
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);

        GiftCertificate createdGiftCertificate = giftCertificate;
        GiftCertificateDto createdGiftCertificateDto = giftCertificateDto;

        given(modelMapper.map(giftCertificateDto, GiftCertificate.class))
                .willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(giftCertificate.getName()))
                .willReturn(null);
        given(giftCertificateRepository.save(giftCertificate))
                .willReturn(createdGiftCertificate);
        given(modelMapper.map(createdGiftCertificate, GiftCertificateDto.class))
                .willReturn(createdGiftCertificateDto);

        giftCertificateService.create(giftCertificateDto);

        then(modelMapper)
                .should(times(1))
                .map(any(GiftCertificateDto.class), eq(GiftCertificate.class));
        then(giftCertificateRepository)
                .should(times(1))
                .findByName(name);
        then(giftCertificateRepository)
                .should(times(1))
                .save(any(GiftCertificate.class));
        then(modelMapper)
                .should(times(1))
                .map(any(GiftCertificate.class), eq(GiftCertificateDto.class));
    }

    @Test
    void create_CertificateExist_ResourceAlreadyExistExceptionThrown() {
        final String name = "gift1";

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName(name);
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);


        GiftCertificate existedGiftCertificate = giftCertificate;

        given(modelMapper.map(giftCertificateDto, GiftCertificate.class))
                .willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(giftCertificate.getName()))
                .willReturn(existedGiftCertificate);

        assertThrows(ResourceAlreadyExistException.class, () -> {
            giftCertificateService.create(giftCertificateDto);
        });

        then(modelMapper)
                .should(only())
                .map(any(GiftCertificateDto.class), eq(GiftCertificate.class));
        then(giftCertificateRepository)
                .should(only())
                .findByName(name);
        then(giftCertificateRepository)
                .should(never())
                .save(any(GiftCertificate.class));
        then(modelMapper)
                .should(never())
                .map(any(GiftCertificate.class), eq(GiftCertificateDto.class));
    }

//    @Test
//    void update_UpdatingCertificate_ReturnUpdatedCertificate() {
//        GiftCertificateUpdateDto giftCertificateUpdateDto = new GiftCertificateUpdateDto();
//        GiftCertificateDto giftCertificateDto = giftCertificateDtoList.get(0);
//        GiftCertificate giftCertificate = giftCertificateList.get(0);
//
//        final Long id = giftCertificate.getId();
//        GiftCertificate existedGiftCertificate = giftCertificate;
//        GiftCertificate updatedGiftCertificate = giftCertificate;
//        GiftCertificateDto updatedGiftCertificateDto = giftCertificateDto;
//
//        given(giftCertificateRepository.findById(id))
//                .willReturn(existedGiftCertificate);
//        given(giftCertificateUpdateConverter.dtoToEntity(giftCertificateUpdateDto))
//                .willReturn(giftCertificate);
//        given(giftCertificateRepository.update(giftCertificate))
//                .willReturn(updatedGiftCertificate);
//        given(giftCertificateConverter.entityToDto(updatedGiftCertificate))
//                .willReturn(updatedGiftCertificateDto);
//
//        GiftCertificateDto actual = giftCertificateService.update(id, giftCertificateUpdateDto);
//
//        assertNotNull(actual);
//        assertEquals(updatedGiftCertificateDto, actual);
//
//        then(giftCertificateRepository)
//                .should(times(1))
//                .findById(anyLong());
//
//        then(giftCertificateUpdateConverter)
//                .should(only())
//                .dtoToEntity(any(GiftCertificateUpdateDto.class));
//
//        then(giftCertificateRepository)
//                .should(times(1))
//                .update(any(GiftCertificate.class));
//
//        then(giftCertificateConverter)
//                .should(only())
//                .entityToDto(any(GiftCertificate.class));
//    }

//    @Test
//    void update_CertificateNotFound_GiftCertificateNotFoundExceptionThrown() {
//        GiftCertificateUpdateDto giftCertificateUpdateDto = new GiftCertificateUpdateDto();
//        final Long id = 1L;
//
//        given(giftCertificateRepository.findById(id)).willReturn(null);
//
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.update(id, giftCertificateUpdateDto);
//        });
//
//        then(giftCertificateRepository)
//                .should(only())
//                .findById(anyLong());
//
//        then(giftCertificateUpdateConverter)
//                .should(never())
//                .dtoToEntity(any(GiftCertificateUpdateDto.class));
//
//        then(giftCertificateRepository)
//                .should(never())
//                .update(any(GiftCertificate.class));
//
//        then(giftCertificateConverter)
//                .should(never())
//                .entityToDto(any(GiftCertificate.class));
//    }

    @Test
    void delete_DeleteCertificate_ReturnNothing() {
        final Long id = 1L;

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(id);
        giftCertificate.setOrderGiftCertificates(new HashSet<>());

        given(giftCertificateRepository.findById(id))
                .willReturn(Optional.of(giftCertificate));
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
    void delete_CertificateNotFound_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(giftCertificateRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
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
    void delete_GiftCertificateUsedByOrder_UnableDeleteResourceExceptionThrown() {
        final Long id = 1L;

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(id);
        giftCertificate.setOrderGiftCertificates(Set.of(new OrderGiftCertificate()));

        given(giftCertificateRepository.findById(id))
                .willReturn(Optional.of(giftCertificate));

        assertThrows(UnableDeleteResourceException.class, () -> {
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
