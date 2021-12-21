package com.aurora.backend.services;

import com.aurora.backend.entity.HasDateCreated;
import com.aurora.backend.entity.HasDateUpdated;
import com.aurora.backend.entity.IEntity;
import com.aurora.backend.entity.JpaEntity;
import com.aurora.backend.entity.core.EntityField;
import com.aurora.backend.exception.PreUpdateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseEntityService<E extends JpaEntity<ID>, ID extends Serializable> extends BaseService {

    private final Class<E> TYPE;

    public BaseEntityService() {
        Type type = getClass().getGenericSuperclass();

        while (!(type instanceof ParameterizedType)) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }

        this.TYPE = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    protected abstract PagingAndSortingRepository<E, ID> getRepository();

    protected abstract void preCreate(final E entity);

    @Transactional
    public E create(final E entity) {
        preCreate(entity);

        if (entity instanceof HasDateCreated) {
            ((HasDateCreated) entity).setDateCreated(LocalDateTime.now());
        }
        if (entity instanceof HasDateUpdated) {
            ((HasDateUpdated) entity).setLastUpdated(LocalDateTime.now());
        }

        E pEntity = getRepository()
                .save(entity);
        postCreate(pEntity);
        return pEntity;
    }

    protected void postCreate(final E entity) {
    }

    protected E preUpdate(final E pEntity, final E entity) {
        for (final Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {

                final Object value = field.get(entity);
                if (value == null) {
                    continue;
                }

                final EntityField e = field.getAnnotation(EntityField.class);
                if (e.isId() || e.isForeignKey()) {
                    continue;
                }

                if (e.canGenericUpdate()) {

                    final String fieldName = field.getName();
                    final Class<?> classType = field.getType();

                    // get persistence value
                    if (isChanged(pEntity.getField(fieldName), value)) {
                        pEntity.setField(fieldName, value, classType);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new PreUpdateException(e.getMessage());
            }
        }
        return pEntity;
    }

    @Transactional
    public E update(final ID id, final E changeSet) {
        assert changeSet != null;
        changeSet.setId(id);
        failIfIdInvalid(changeSet.getId());

        E pEntity = this.getById(changeSet.getId());

        // Validation and update of the changed attributes
        pEntity = preUpdate(pEntity, changeSet);

        assert pEntity != null;
        pEntity.setLastUpdated(LocalDateTime.now());
        pEntity = getRepository()
                .save(pEntity);

        postUpdate(pEntity);
        return pEntity;
    }

    protected void postUpdate(final E pEntity) {
    }

    @Transactional(readOnly = true)
    public E getById(final ID id) {
        failIfIdInvalid(id);
        return failIfNotFound(id);
    }

    protected void failIfBlank(final String value, final String args) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(format("%s %s cannot be null/blank", TYPE.getSimpleName(), args));
        }
    }

    protected void failIfBlank(final String value, final String args, final Class<?> clazz) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(format("%s %s cannot be null/blank", clazz.getSimpleName(), args));
        }
    }

    protected E failIfNotFound(final ID id) {
        return this
                .getRepository()
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("%s (ID %s) not found", TYPE.getSimpleName(), id)));
    }

    protected void failIfIdInvalid(final ID id) {
        if (id instanceof Number) {
            if (((Number) id).longValue() <= 0) {
                throw new IllegalArgumentException(format("%s invalid ID", TYPE.getSimpleName()));
            }
        } else if (id instanceof String) {
            if (isBlank((String) id)) {
                throw new IllegalArgumentException(format("%s invalid ID", TYPE.getSimpleName()));
            }
        } else {
            throw new IllegalArgumentException(format("%s invalid ID", TYPE.getSimpleName()));
        }
    }

    protected void failConstraintUnless(final boolean expression, final String reason) {
        if (!expression) {
            throw new IllegalArgumentException(reason);
        }
    }

    protected void failInvalidUnless(final boolean expression, final String name) {
        if (!expression) {
            throw new IllegalArgumentException(format("%s %s is invalid", TYPE.getSimpleName(), name));
        }
    }

    protected void failIfNull(final Object value, final String args) {
        if (value == null) {
            throw new IllegalArgumentException(format("%s %s cannot be null", TYPE.getSimpleName(), args));
        }
    }

    protected void failIfAlphabet(final String value, final String args) {
        failIfAlphabet(value, args, false);
    }

    protected void failIfAlphabet(final String value, final String args, final Boolean allowBlankField) {

        if (allowBlankField) {
            if (StringUtils.isNotBlank(value)) {
                if (!StringUtils.isNumeric(value)) {
                    throw new IllegalArgumentException(format("%s %s cannot be alphabet", TYPE.getSimpleName(), args));
                }
            }
        } else {
            if (!StringUtils.isNumeric(value)) {
                throw new IllegalArgumentException(format("%s %s cannot be alphabet", TYPE.getSimpleName(), args));
            }
        }
    }

    protected void failIfEmpty(Object value, String args) {
        if (value instanceof List) {
            List l = (List) value;
            if (l.isEmpty()) {
                throw new IllegalArgumentException(String.format("%s %s cannot be empty", TYPE.getSimpleName(), args));
            }
        } else if (value instanceof Set) {
            Set s = (Set) value;
            if (s.isEmpty()) {
                throw new IllegalArgumentException(String.format("%s %s cannot be empty", TYPE.getSimpleName(), args));
            }
        } else {
            throw new IllegalArgumentException("Value is not a list or set!");
        }
    }

    protected boolean isChanged(Object persistentObj, Object incoming) {
        return isChanged(persistentObj, incoming, false);
    }

    protected boolean isChanged(Object persistentObj, Object incoming, Boolean allowBlankField) {

        if (!allowBlankField) {
            if (incoming instanceof String && StringUtils.isBlank((String) incoming)) {
                return false;
            }
        }

        if (persistentObj == null && incoming == null) {
            return false;
        }
        if (persistentObj == null && incoming != null) {
            return true;
        }
        // If incoming change is NULL, assume no change!
        if (persistentObj != null && incoming == null) {
            return false;
        }

        if (persistentObj
                .getClass()
                .equals(incoming.getClass())) {
            if (persistentObj instanceof IEntity) {
                return ((IEntity) persistentObj).getId() != ((IEntity) incoming).getId();
            } else {
                return !persistentObj.equals(incoming);
            }
        }
        // FIXME: Hackish way to deal with util.date vs. mysql.timestamp
        else if (incoming instanceof Date) {
            return !(((Timestamp) persistentObj).getTime() == ((Date) incoming).getTime());
        } else {
            // TODO: maybe returning false?
            throw new UnsupportedOperationException();
        }
    }

}
