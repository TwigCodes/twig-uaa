package com.twigcodes.uaa.service;

import com.twigcodes.uaa.domain.User;
import com.twigcodes.uaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("未找到用户名" + username));
    }
}
