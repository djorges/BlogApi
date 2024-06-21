package org.example.blogapi;

import org.example.blogapi.entity.PermissionEntity;
import org.example.blogapi.entity.RoleEntity;
import org.example.blogapi.entity.RoleEnum;
import org.example.blogapi.entity.UserEntity;
import org.example.blogapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BlogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApiApplication.class, args);
    }

    @Autowired
    private IUserRepository repository;

    @Bean
    CommandLineRunner init(){
        return args -> {
            //Create Permissions
            PermissionEntity createPermission = PermissionEntity.builder()
                    .name("CREATE")
                    .build();
            PermissionEntity readPermission = PermissionEntity.builder()
                    .name("READ")
                    .build();
            PermissionEntity updatePermission = PermissionEntity.builder()
                    .name("UPDATE")
                    .build();
            PermissionEntity deletePermission = PermissionEntity.builder()
                    .name("DELETE")
                    .build();
            PermissionEntity refactorPermission = PermissionEntity.builder()
                    .name("REFACTOR")
                    .build();
            //Create ROLES
            RoleEntity roleAdmin = RoleEntity.builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permissionList(Set.of(createPermission,readPermission,updatePermission,deletePermission))
                    .build();
            RoleEntity roleUser = RoleEntity.builder()
                    .roleEnum(RoleEnum.USER)
                    .permissionList(Set.of(createPermission,readPermission))
                    .build();
            RoleEntity roleInvited = RoleEntity.builder()
                    .roleEnum(RoleEnum.INVITED)
                    .permissionList(Set.of(readPermission))
                    .build();
            RoleEntity roleDeveloper = RoleEntity.builder()
                    .roleEnum(RoleEnum.DEVELOPER)
                    .permissionList(Set.of(createPermission,readPermission,updatePermission,deletePermission,refactorPermission))
                    .build();
            // Create USERS
            UserEntity userJoe = UserEntity.builder()
                    .username("joe")
                    .password("$2a$10$4ATFsremuqgDueJ15euXieU.l69Qkt7AY/N0ERk0PAgA/5Ikb12EK")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleAdmin))
                    .build();
            UserEntity userJack = UserEntity.builder()
                    .username("jack")
                    .password("$2a$10$QAzl5TaO7CW7z/N6.51CzOl2e.dtXw6WXBsZUmSzjwiL2xjQb74KG")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleUser))
                    .build();
            UserEntity userSara = UserEntity.builder()
                    .username("sara")
                    .password("$2a$10$ll.V.joApkvzVhdrQkXYDOVvshvilEB2t197hDyf8N9yV6cFitVtO")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleInvited))
                    .build();
            UserEntity userLucas = UserEntity.builder()
                    .username("lucas")
                    .password("$2a$10$QwDpe3sSi43YiTyVQobv8Oyh/nup2b0SKD3fkKD7Xq4MYdNIp43my")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleDeveloper))
                    .build();

            repository.saveAll(List.of(userJoe,userJack,userSara,userLucas));
        };
    }

}
