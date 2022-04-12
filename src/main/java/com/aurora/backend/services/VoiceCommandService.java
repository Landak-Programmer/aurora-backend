package com.aurora.backend.services;

import com.aurora.backend.entity.VoiceCommand;
import com.aurora.backend.exception.IllegalCommandParamException;
import com.aurora.backend.exception.UnknownVoiceCommandException;
import com.aurora.backend.repo.VoiceCommandRepository;
import com.aurora.backend.services.internal.VoiceCommandInternalService;
import com.aurora.backend.utils.VoiceCommandHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoiceCommandService extends BaseEntityService<VoiceCommand, Long> {

    @Autowired
    private VoiceCommandRepository voiceCommandRepository;
    @Autowired
    private VoiceCommandInternalService voiceCommandInternalService;

    @Override
    protected PagingAndSortingRepository<VoiceCommand, Long> getRepository() {
        return voiceCommandRepository;
    }

    @Override
    protected void preCreate(VoiceCommand entity) {

    }

    public VoiceCommand processCommand(final String fullCommand) {
        final List<String> partialTextCommand = Arrays.asList(fullCommand.split("\\s+"));
        if (partialTextCommand.size() < 2) {
            throw new UnknownVoiceCommandException("Command argument is less than expected!");
        }
        final String prefixCommand = String.format("%s %s", partialTextCommand.get(0), partialTextCommand.get(1));
        final List<VoiceCommand> qualifiedCommand = getListOfQualifiedCommand(prefixCommand);

        if (qualifiedCommand.size() == 0) {
            throw new UnknownVoiceCommandException("Unable to determine command!");
        } else if (qualifiedCommand.size() == 1) {

            final VoiceCommand voiceCommand = qualifiedCommand.get(0);
            final List<String> params = partialTextCommand.subList(2, partialTextCommand.size());
            switch (voiceCommand.getResponseType()) {
                case EXTERNAL:
                    voiceCommand.setParams(VoiceCommandHelper.assignValueToKey(params, voiceCommand.getParamTag()));
                    return voiceCommand;
                case INTERNAL:
                case MIXED:
                    return processInternal(voiceCommand, params);
                default:
                    throw new UnknownVoiceCommandException("No such processing command of " + voiceCommand.getResponseType());
            }
        } else {
            throw new UnsupportedOperationException("Move than 2 qualified command detected!");
        }
    }

    private VoiceCommand processInternal(final VoiceCommand voiceCommand, final List<String> params) {
        return voiceCommandInternalService.doInternalProcess(voiceCommand, params);
    }

    private List<VoiceCommand> getListOfQualifiedCommand(final String prefixCommand) {
        return voiceCommandRepository.findByCommandStartsWith(prefixCommand);
    }
}
