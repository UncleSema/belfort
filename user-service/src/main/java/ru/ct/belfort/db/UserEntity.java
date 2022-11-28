package ru.ct.belfort.db;


import lombok.Builder;

@Builder
public record UserEntity(int id, String str) {
}
