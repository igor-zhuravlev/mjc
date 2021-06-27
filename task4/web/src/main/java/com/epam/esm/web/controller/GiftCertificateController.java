package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import com.epam.esm.web.constant.SecurityExpression;
import com.epam.esm.web.hateoas.GiftCertificateLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

/**
 * The Gift Certificate Controller represents user api for GiftCertificate
 */

@RestController
@RequestMapping("/gifts")
@Validated
public class GiftCertificateController {

    @Autowired
    private GiftCertificateService giftCertificateService;
    @Autowired
    private GiftCertificateLinkBuilder giftCertificateLinkBuilder;

    /**
     * Finds all gift certificates with tags
     * @param giftCertificateParam gift certificate search parameters which passed in request
     * @param page requested page
     * @param assembler {@link PagedResourcesAssembler} for convert Page into PagedResources
     * @return list of gift certificates dto
     */

    @GetMapping
    public PagedModel<EntityModel<GiftCertificateDto>> findAll(@Valid GiftCertificateParamDto giftCertificateParam,
                                                               Pageable page,
                                                               PagedResourcesAssembler<GiftCertificateDto> assembler) {
        Page<GiftCertificateDto> giftCertificateDtoPage =
                giftCertificateService.findAll(giftCertificateParam, page);
        return assembler.toModel(giftCertificateDtoPage);
    }

    /**
     * Finds a gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @return the gift certificate dto if found
     */

    @GetMapping("/{id}")
    public GiftCertificateDto find(@PathVariable @Positive Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        giftCertificateLinkBuilder.addGiftCertificateLinks(giftCertificateDto);
        return giftCertificateDto;
    }

    /**
     * Creates the gift certificate
     * @param giftCertificateDto gift certificate dto passed in request
     * @return saved gift certificate dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @PostMapping
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        giftCertificateLinkBuilder.addCretedGiftCertificateLinks(createdGiftCertificateDto);
        return createdGiftCertificateDto;
    }

    /**
     * Updates the gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @param giftCertificateDto gift certificate dto with updates
     * @return updated gift certificate dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @PatchMapping("/{id}")
    public GiftCertificateDto update(@PathVariable @Positive Long id,
                                     @RequestBody @Valid GiftCertificateUpdateDto giftCertificateDto) {
        GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(id, giftCertificateDto);
        giftCertificateLinkBuilder.addUpdatedGiftCertificateLinks(updatedGiftCertificateDto);
        return updatedGiftCertificateDto;
    }

    /**
     * Deletes the gift certificate by id
     * @param id identifier of the gift certificate
     * @return Object with code 200
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok().build();
    }
}
