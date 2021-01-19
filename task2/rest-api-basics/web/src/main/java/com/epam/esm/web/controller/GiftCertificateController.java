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

    @GetMapping
    public List<GiftCertificateDto> findAllGiftCertificates(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return giftCertificateService.findAll(multiValueMapToMap(requestParams));
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findGiftCertificateById(@PathVariable Long id) {
        return giftCertificateService.findById(id);
    }

    @PostMapping
    public GiftCertificateDto saveGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.save(giftCertificateDto);
    }

    @PutMapping("/{id}")
    public GiftCertificateDto updateGiftCertificateById(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGiftCertificateById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
    }
}
