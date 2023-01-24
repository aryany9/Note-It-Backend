package com.note.it.Entities.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.note.it.Constants.DataFormatConstants;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class AbstractBaseAuditable {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    @CsvBindByName(column = "Created On", required = false)
    @CsvDate(value = DataFormatConstants.TIMESTAMP_INPUT_FORMAT)
    @JsonFormat(pattern = DataFormatConstants.TIMESTAMP_OUTPUT_FORMAT)
    private Calendar createdOn;

    @LastModifiedBy
    private String updatedBy;

    @Column(nullable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = DataFormatConstants.TIMESTAMP_OUTPUT_FORMAT)
    private Calendar updatedOn;
}
