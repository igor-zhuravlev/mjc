package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
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

    private Map<String, String[]> multiValueMapToMap(MultiValueMap<String, String> requestParams) {
        return requestParams.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().toArray(String[]::new)
                ));
    }

    /**
     * Finds all gift certificates with tags
     * @param requestParams request params in url such as:
     *                      <p>tag - tag name</p>
     *                      <p>part - part of name, description of the gift certificates</p>
     *                      <p>sort - sort by name or/and date with asc or desc direction</p>
     *
     *                      <p>example: ?tag=tagName&part=partName&sort=name,asc&sort=date,desc</p>
     *
     *                      <p>request params aren't required</p>
     * @return list of gift certificates dto
     */

    @GetMapping
    public List<GiftCertificateDto> findAll(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return giftCertificateService.findAll(multiValueMapToMap(requestParams));
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
     * Saves the gift certificate
     * @param giftCertificateDto gift certificate dto passed in request
     * @return saved gift certificate dto
     */

    @PostMapping
    public GiftCertificateDto save(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.save(giftCertificateDto);
    }

    /**
     * Updates the gift certificate by id
     * @param id identifier of the gift certificate. Id is required in url path.
     * @param giftCertificateDto gift certificate dto with updates
     * @return updated gift certificate dto
     */

    @PutMapping("/{id}")
    public GiftCertificateDto update(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    /**
     * Deletes the gift certificate by id
     * @param id identifier of the gift certificate
     */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
    }
}
