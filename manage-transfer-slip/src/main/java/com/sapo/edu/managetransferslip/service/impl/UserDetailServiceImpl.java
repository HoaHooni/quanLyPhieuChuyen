package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.entity.UsersEntity;
import com.sapo.edu.managetransferslip.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UsersEntity user = userRepository.findByUsername(s);
        if(user ==null){
            throw new UsernameNotFoundException("err");
        }
        return UserDetailImpl.build(user);
    }
}
