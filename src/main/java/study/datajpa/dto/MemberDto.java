package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {

    private String username;
    private Long id;
    private String teamName;

    public MemberDto(String username, Long id, String teamName) {
        this.username = username;
        this.id = id;
        this.teamName = teamName;
    }

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();

    }
}
