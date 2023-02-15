package com.cn.xiguaapp.r2dbc.orm.rdb.utils;


import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.meta.FeatureSupportedMetadata;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @desc feature工具类
 * @since 1.0.0
 */
public class FeatureUtils {

    private static boolean r2dbcIsAlive;

    static {
        try {
            //初始化R2dbc连接
            Class.forName("io.r2dbc.spi.Connection");
            r2dbcIsAlive = true;
        } catch (ClassNotFoundException e) {
            r2dbcIsAlive = false;
        }

    }

    /**
     * 是否支持R2dbc
     * @return boolean
     */
    public static boolean r2dbcIsAlive() {
        return r2dbcIsAlive;
    }

    /**
     * 将feature类转换为String
     * @param features features
     * @see Feature
     * @return String
     */
    public static String featureToString(List<Feature> features) {
        StringBuilder builder = new StringBuilder();

        Map<FeatureType, List<Feature>> featureMap = features
                .stream()
                .collect(Collectors.groupingBy(Feature::getType
                        , () -> new TreeMap<>(Comparator.comparing(FeatureType::getId))
                        , Collectors.toList()));

        for (Map.Entry<FeatureType, List<Feature>> entry : featureMap.entrySet()) {
            builder.append("--").append(entry.getKey().getId())
                    .append(" (").append(entry.getKey().getName())
                    .append(")")
                    .append("\n");
            for (Feature feature : entry.getValue()) {
                builder.append("-----|---- ")
                        .append(feature.getId())
                        .append(" (")
                        .append(feature.getName())
                        .append(")")
                        .append("\t").append(feature.getClass().getSimpleName())
                        .append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * 将ObjectMetaData转化为String
     * @param metadata metadata
     * @see ObjectMetadata
     * @return String
     */
    public static String metadataToString(ObjectMetadata metadata) {
        StringBuilder builder = new StringBuilder();

        builder.append(metadata.getObjectType().getId())
                .append("(")
                .append(metadata.getObjectType().getName())
                .append(")");

        if (metadata instanceof FeatureSupportedMetadata) {
            builder.append("\n")
                    .append(featureToString(((FeatureSupportedMetadata) metadata).getFeatureList()));
        }


        return builder.toString();

    }

}
