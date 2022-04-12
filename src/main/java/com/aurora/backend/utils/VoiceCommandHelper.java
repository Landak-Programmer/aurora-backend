package com.aurora.backend.utils;

import com.aurora.backend.exception.IllegalCommandParamException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceCommandHelper {

    public static Map<String, String> assignValueToKey(final List<String> params, final String paramTag) {
        final Map<String, String> map = new HashMap<>();
        final List<String> tag = Arrays.asList(paramTag.split(","));
        if (tag.size() != params.size()) {
            throw new IllegalCommandParamException(String.format("Number of argument for param and tag are not equal. " +
                    "Param size:%s, Tag size:%s", params.size(), tag.size()));
        }

        int itr = 0;
        for (final String s : params) {
            map.put(tag.get(itr), s);
            itr++;
        }
        return map;
    }
}
