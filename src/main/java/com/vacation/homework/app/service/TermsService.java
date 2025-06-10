package com.vacation.homework.app.service;

import com.vacation.homework.app.domain.Terms;
import com.vacation.homework.app.dto.TermsDto;
import com.vacation.homework.app.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    @Value("${app.terms.version}")
    private String termsVersion;

    public List<TermsDto> getLatestTerms() {
        return termsRepository.findTermsByVersion(termsVersion)
                .stream()
                .map(TermsDto::from)
                .toList();
    }

}