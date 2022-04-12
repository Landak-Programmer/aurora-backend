package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;

// todo: convert me to elasticsearch!
@Getter
@Setter
@Entity
@Table(name = "voice_command")
public class VoiceCommand extends JpaEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EntityField(isId = true)
    private Long id;

    @NotNull
    @EntityField
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String command;

    @NotNull
    @EntityField
    private String responseVoice;

    @NotNull
    @EntityField
    private String responseAction;

    @NotNull
    @EntityField
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ResponseType responseType;

    @ManyToOne
    @JoinColumn(name = "precede_command_id")
    @EntityField
    private VoiceCommand precedeCommand;

    @EntityField
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String paramTag;

    @NotNull
    @EntityField
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isChain;

    @Transient
    private Map<String, String> params = new HashMap<>();

    public enum ResponseType {
        INTERNAL,
        EXTERNAL,
        MIXED
    }

}
