package com.epam.esm.web.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/tags")
    public List<GiftCertificateDto> findAllGiftCertificatesWithTags(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return giftCertificateService.findAllWithTags(multiValueMapToMap(requestParams));
    }

    @GetMapping
    public List<GiftCertificateDto> findAllGiftCertificates() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/tag/{name}")
    public List<GiftCertificateDto> findGiftCertificatesByTag(@PathVariable String name) {
        return giftCertificateService.findByTagName(name);
    }

    @GetMapping("/{name}")
    public GiftCertificateDto findGiftCertificateByName(@PathVariable String name) {
        return giftCertificateService.findByName(name);
    }

    @PostMapping
    public GiftCertificateDto saveGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.save(giftCertificateDto);
    }

    @PutMapping("/{name}")
    public GiftCertificateDto updateGiftCertificate(@PathVariable String name, @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateByName(name, giftCertificateDto);
    }

    @DeleteMapping("/{name}")
    public void deleteGiftCertificateByName(@PathVariable String name) {
        giftCertificateService.deleteByName(name);
    }
}
