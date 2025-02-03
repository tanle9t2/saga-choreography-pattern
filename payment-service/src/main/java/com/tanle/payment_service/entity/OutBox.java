package com.tanle.payment_service.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanle.payment_service.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "out_box_payment")
public class OutBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "aggregate_id")
    private String aggregateId;
    @Column(name = "payload",columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode payload;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "is_process")
    private boolean isProcess;
}
