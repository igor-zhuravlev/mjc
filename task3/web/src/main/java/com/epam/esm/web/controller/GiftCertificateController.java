package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Gift Certificate Controller represents user api for GiftCertificate
 */

@RestController
@RequestMapping("/gifts")
public class GiftCertificateController {

    @Autowired
    private GiftCertificateService giftCertificateService;

    /**
     * Finds all gift certificates with tags
     * @param requestParams search params in url such as:
     *                      <p>name - part of the certificate name</p>
     *                      <p>description - part of the certificate description</p>
     *                      <p>tags - list of tag names</p>
     *                      <p>sort - sort by name or/and date with asc or desc direction</p>
     *
     *                      <p>example: ?name=partName&description=partDesc&tags=tagName1,tagName2&sort=name,asc&sort=create_date,desc</p>
     *
     *                      <p>request params aren't required</p>
     * @param size count of gift certificates on page
     * @param page number of page
     * @return list of gift certificates dto
     */

    @GetMapping
    public CollectionModel<GiftCertificateDto> findAll(@RequestParam(required = false) MultiValueMap<String, String> requestParams,
                                                       @RequestParam(required = false, defaultValue = "5") Integer size,
                                                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<GiftCertificateDto> giftCertificateDtoList =
                giftCertificateService.findAll(multiValueMapToMap(requestParams), pageDto);
        return CollectionModel.of(giftCertificateDtoList);
    }

    /**
     * Finds a gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @return the gift certificate dto if found
     */

    @GetMapping("/{id}")
    public GiftCertificateDto find(@PathVariable Long id) {
        return giftCertificateService.findById(id);
    }

    /**
     * Creates the gift certificate
     * @param giftCertificateDto gift certificate dto passed in request
     * @return saved gift certificate dto
     */

    @PostMapping
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.create(giftCertificateDto);
    }

    /**
     * Updates the gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @param giftCertificateDto gift certificate dto with updates
     * @return updated gift certificate dto
     */

    @PatchMapping("/{id}")
    public GiftCertificateDto update(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.update(id, giftCertificateDto);
    }

    /**
     * Deletes the gift certificate by id
     * @param id identifier of the gift certificate
     * @return empty response with code 204
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, String[]> multiValueMapToMap(MultiValueMap<String, String> requestParams) {
        return requestParams.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().toArray(String[]::new)
                ));
    }
}
