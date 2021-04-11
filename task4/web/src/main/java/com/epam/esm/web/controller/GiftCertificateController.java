package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import com.epam.esm.web.constant.ApiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The Gift Certificate Controller represents user api for GiftCertificate
 */

@RestController
@RequestMapping("/gifts")
@Validated
public class GiftCertificateController {

    @Autowired
    private GiftCertificateService giftCertificateService;

    /**
     * Finds all gift certificates with tags
     * @param giftCertificateParam gift certificate search parameters which passed in request
     * @param page requested page
     * @return list of gift certificates dto
     */

    @GetMapping
    public Page<GiftCertificateDto> findAll(@Valid GiftCertificateParamDto giftCertificateParam, Pageable page) {
        Page<GiftCertificateDto> giftCertificateDtoPage =
                giftCertificateService.findAll(giftCertificateParam, page);
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .findAll(giftCertificateParam, page))
                .withSelfRel();
        return giftCertificateDtoPage;
    }

    /**
     * Finds a gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @return the gift certificate dto if found
     */

    @GetMapping("/{id}")
    public GiftCertificateDto find(@PathVariable @Positive Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(id))
                .withSelfRel());
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(id, null))
                .withRel(ApiConstant.UPDATE));
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(id))
                .withRel(ApiConstant.DELETE));
        return giftCertificateDto;
    }

    /**
     * Creates the gift certificate
     * @param giftCertificateDto gift certificate dto passed in request
     * @return saved gift certificate dto
     */

    @PreAuthorize("hasRole(T(com.epam.esm.domain.entity.Role).ADMIN)")
    @PostMapping
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        createdGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(createdGiftCertificateDto.getId()))
                .withRel(ApiConstant.FIND));
        createdGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(createdGiftCertificateDto.getId(), null))
                .withRel(ApiConstant.UPDATE));
        createdGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(createdGiftCertificateDto.getId()))
                .withRel(ApiConstant.DELETE));
        return createdGiftCertificateDto;
    }

    /**
     * Updates the gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @param giftCertificateDto gift certificate dto with updates
     * @return updated gift certificate dto
     */

    @PreAuthorize("hasRole(T(com.epam.esm.domain.entity.Role).ADMIN)")
    @PatchMapping("/{id}")
    public GiftCertificateDto update(@PathVariable @Positive Long id,
                                     @RequestBody @Valid GiftCertificateUpdateDto giftCertificateDto) {
        GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(id, giftCertificateDto);
        updatedGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(id, giftCertificateDto))
                .withSelfRel());
        updatedGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(id))
                .withRel(ApiConstant.FIND));
        updatedGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(id))
                .withRel(ApiConstant.DELETE));
        return updatedGiftCertificateDto;
    }

    /**
     * Deletes the gift certificate by id
     * @param id identifier of the gift certificate
     * @return Object with code 200
     */

    @PreAuthorize("hasRole(T(com.epam.esm.domain.entity.Role).ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok().build();
    }
}
