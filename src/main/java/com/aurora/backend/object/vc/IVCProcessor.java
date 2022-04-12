package com.aurora.backend.object.vc;

import com.aurora.backend.entity.VoiceCommand;
import com.aurora.backend.utils.VoiceCommandHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class IVCProcessor {

    public VoiceCommand execute(final VoiceCommand voiceCommand, final List<String> params)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final List<String> result = new ArrayList<>();
        int itr = 1;
        int sizeItr = 0;
        StringBuilder find = new StringBuilder();
        for (final String s : params) {
            if (this.limiter().contains(s) || (sizeItr == params.size() - 1)) {
                final Method method = this
                        .getClass()
                        .getMethod(constructMethodName(itr), String.class);
                String item = (String) method.invoke(this, find.toString().trim());
                find = new StringBuilder();
                itr++;
                result.add(item);
                continue;
            }
            find.append(s);
            find.append(" ");
            sizeItr++;
        }
        voiceCommand.setParams(VoiceCommandHelper.assignValueToKey(result, voiceCommand.getParamTag()));
        return voiceCommand;
    }

    public abstract String idArgs();

    public abstract String internal1(final String args);

    protected List<String> limiter() {
        return new ArrayList<>(Collections.singletonList("comma"));
    }

    protected String constructMethodName(final int itr) {
        final String methodKey = "internal";
        return String.format("%s%s", methodKey, itr);
    }
}
