package com.note.it.Entities.base;

import com.note.it.Utilities.UUIDUtils;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)

/*
 * @MappedSuperclass is an annotation in JPA (Java Persistence API)
 * that is used to indicate that a class should be used as a superclass
 * for other entity classes. When a class is marked with the @MappedSuperclass annotation,
 * its properties and fields are not considered to be part of the entity's own table,
 * but they can be used in the entity classes that inherit from it.
 * Any class that is annotated with @MappedSuperclass cannot be used
 * as an Entity class by itself. This means it cannot be queried or persisted
 * in the database, it is just a way to share common fields or properties
 * among multiple entities.
 * It is useful when you have common fields or properties that you want
 * to reuse across multiple entities, and you want to avoid duplication of code.
 * This way you can define these fields or properties in a single class,
 * and all the entities that inherit from it will have these fields or properties.
 */
@MappedSuperclass
@AllArgsConstructor
public class BaseAuditableUUID extends AbstractBaseAuditable implements Serializable {
    private UUID id;

    public BaseAuditableUUID() {
        super();
        id = UUIDUtils.generate();
    }
}
