package com.example.assignment.member.repository;

import com.example.assignment.common.exception.custom.InvalidCredentialsException;
import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemberRepository {
    private final ConcurrentHashMap<String, Member> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public void save(Member member) {
        long newId = sequence.getAndIncrement();
        member.setId(newId);
        store.put(member.getEmail(), member);
    }

    public Member findByEmailOrThrow(String email) {
        return Optional.ofNullable(store.get(email))
                .orElseThrow(InvalidCredentialsException::new);
    }

    public boolean existsByEmail(String email) {
        return store.containsKey(email);
    }
    
    public List<Member> findAll() {
        return List.copyOf(store.values());
    }

    public void deleteByEmail(String email) {
        store.remove(email);
    }

    public Member updateRole(String email, Role newRole) {
        return Optional.ofNullable(store.computeIfPresent(email, (k, member) -> {
            member.setRole(newRole);
            return member;
        })).orElseThrow(InvalidCredentialsException::new);
    }
}