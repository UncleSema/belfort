package ru.ct.belfort.db;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record IdeaEntity(int id, double score, Timestamp time) {
}
