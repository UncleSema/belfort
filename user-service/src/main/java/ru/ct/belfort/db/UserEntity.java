package ru.ct.belfort.db;


import lombok.Builder;

@Builder
public record UserEntity(long id, String str) {
}
