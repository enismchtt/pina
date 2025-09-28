package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Configuration;

import java.util.List;
import java.util.Optional;

public interface IConfigurationService {
    List<Configuration> findAll();
    Optional<Configuration> findById(Long id);
    Optional<Configuration> findByConfigKey(String configKey);
    Configuration save(Configuration configuration);
    void deleteById(Long id);
    String getConfigValue(String configKey, String defaultValue);
    void updateConfigValue(String configKey, String configValue);
}
