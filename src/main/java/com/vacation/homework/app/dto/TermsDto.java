package com.vacation.homework.app.dto;
import com.vacation.homework.app.domain.Terms;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "약관 정보 DTO")
public class TermsDto {

    @Schema(description = "약관 제목")
    private String title;

    @Schema(description = "약관 내용")
    private String content;

    @Schema(description = "버전")
    private String version;

    public static TermsDto from(Terms terms) {
        return TermsDto.builder()
                .title(terms.getTitle())
                .content(terms.getContent())
                .version(terms.getVersion())
                .build();
    }
}