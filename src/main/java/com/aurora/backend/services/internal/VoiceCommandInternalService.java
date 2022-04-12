package com.aurora.backend.services.internal;

import com.aurora.backend.entity.VoiceCommand;
import com.aurora.backend.exception.VCProcessorException;
import com.aurora.backend.object.vc.IVCProcessor;
import com.aurora.backend.object.vc.VCWhatsappProcessor;
import com.aurora.backend.utils.VoiceCommandHelper;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class VoiceCommandInternalService extends VoiceCommandInternalBaseService {

    public VoiceCommandInternalService(VCWhatsappProcessor vcWhatsappProcessor) {
        super(vcWhatsappProcessor);
    }

    public VoiceCommand doInternalProcess(final VoiceCommand voiceCommand, final List<String> params) {
        // todo: ugly?
        for (final IVCProcessor p : allProcessor) {
            if (voiceCommand.getResponseAction().equalsIgnoreCase(p.idArgs())) {
                try {
                    return p.execute(voiceCommand, params);
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                    throw new VCProcessorException("Unable to execute processor due to " + e.getMessage());
                }
            }
        }
        // default. todo: clarify
        voiceCommand.setParams(VoiceCommandHelper.assignValueToKey(params, voiceCommand.getParamTag()));
        return voiceCommand;
    }
}
