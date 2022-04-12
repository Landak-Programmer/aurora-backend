package com.aurora.backend.services.internal;

import com.aurora.backend.object.vc.IVCProcessor;
import com.aurora.backend.object.vc.VCWhatsappProcessor;
import com.aurora.backend.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * todo: Ugly, make me dynamic
 */
public abstract class VoiceCommandInternalBaseService extends BaseService {

    protected VCWhatsappProcessor vcWhatsappProcessor;

    protected Set<IVCProcessor> allProcessor = new HashSet<>();

    @Autowired
    public VoiceCommandInternalBaseService(
            final VCWhatsappProcessor vcWhatsappProcessor) {
        this.vcWhatsappProcessor = vcWhatsappProcessor;
        allProcessor.add(vcWhatsappProcessor);
    }

}
