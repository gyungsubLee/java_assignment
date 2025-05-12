package com.example.assignment.member.service;

import com.example.assignment.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberRepository {
    private final Map<String, Member> store = new HashMap<>();

    public void save(Member member) {
        store.put(member.getEmail(), member);
    }

    public boolean findByEmail(String email) {
        return store.containsKey(email);
    }
}