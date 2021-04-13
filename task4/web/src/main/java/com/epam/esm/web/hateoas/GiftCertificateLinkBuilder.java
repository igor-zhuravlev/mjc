package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.web.constant.ApiConstant;
import com.epam.esm.web.controller.GiftCertificateController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateLinkBuilder {

    public void addGiftCertificateLinks(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(giftCertificateDto.getId()))
                .withSelfRel());
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(giftCertificateDto.getId(), null))
                .withRel(ApiConstant.UPDATE));
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(giftCertificateDto.getId()))
                .withRel(ApiConstant.DELETE));
    }

    public void addCretedGiftCertificateLinks(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(giftCertificateDto.getId()))
                .withRel(ApiConstant.FIND));
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(giftCertificateDto.getId(), null))
                .withRel(ApiConstant.UPDATE));
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(giftCertificateDto.getId()))
                .withRel(ApiConstant.DELETE));
    }

    public void addUpdatedGiftCertificateLinks(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .update(giftCertificateDto.getId(), null))
                .withSelfRel());
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .find(giftCertificateDto.getId()))
                .withRel(ApiConstant.FIND));
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(giftCertificateDto.getId()))
                .withRel(ApiConstant.DELETE));
    }
}
