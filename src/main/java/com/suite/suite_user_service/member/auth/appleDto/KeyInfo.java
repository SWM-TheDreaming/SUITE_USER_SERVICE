package com.suite.suite_user_service.member.auth.appleDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeyInfo {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;

    public KeyInfo(String kty, String kid, String use, String alg, String n, String e) {
        this.kty = kty;
        this.kid = kid;
        this.use = use;
        this.alg = alg;
        this.n = n;
        this.e = e;
    }

    public boolean validateKey(String kid, String alg) {
        if(this.kid.equals(kid) && this.alg.equals(alg)) return true;
        return false;
    }
}
