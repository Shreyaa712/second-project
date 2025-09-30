// ===== PROJECT STRUCTURE =====
/*
rockfall-prediction-system/
├── src/main/java/com/mining/rockfall/
│   ├── RockfallPredictionApplication.java
│   ├── config/
│   │   ├── DatabaseConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── MonitoringController.java
│   │   ├── AlertController.java
│   │   └── DashboardController.java
│   ├── service/
│   │   ├── RockfallPredictionService.java
│   │   ├── SensorDataProcessor.java
│   │   ├── MachineLearningEngine.java
│   │   ├── AlertService.java
│   │   └── HistoricalAnalysisService.java
│   ├── model/
│   │   ├── SensorReading.java
│   │   ├── RockfallIncident.java
│   │   ├── RiskAssessment.java
│   │   ├── MineStatus.java
│   │   └── AlertNotification.java
│   ├── repository/
│   │   ├── SensorReadingRepository.java
│   │   ├── RockfallIncidentRepository.java
│   │   └── AlertRepository.java
│   ├── util/
│   │   ├── DataProcessor.java
│   │   ├── SignalAnalyzer.java
│   │   └── Constants.java
│   └── dto/
│       ├── SensorDataRequest.java
│       ├── RiskResponse.java
│       └── AlertRequest.java
├── src/main/resources/
│   ├── application.yml
│   ├── data.sql
│   └── ml-models/
│       └── rockfall-classifier.model
└── pom.xml
*/

// ===== 1. MAIN APPLICATION CLASS =====
package com.mining.rockfall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class RockfallPredictionApplication implements WebMvcConfigurer {
    
    public static void main(String[] args) {
        SpringApplication.run(RockfallPredictionApplication.class, args);
        System.out.println("🏔️ Rockfall Prediction System Started Successfully!");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

// ===== 2. ENTITY MODELS =====

// SensorReading.java
package com.mining.rockfall.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sensor_id")
    private String sensorId;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "vibration_level")
    private double vibrationLevel; // in Hz
    
    @Column(name = "temperature")
    private double temperature; // in Celsius
    
    @Column(name = "moisture_level")
    private double moistureLevel; // percentage
    
    @Column(name = "pressure")
    private double pressure; // in kPa
    
    @Column(name = "location_x")
    private double locationX;
    
    @Column(name = "location_y")
    private double locationY;
    
    // Constructors
    public SensorReading() {}
    
    public SensorReading(String sensorId, double vibrationLevel, 
                        double temperature, double moistureLevel, 
                        double pressure, double locationX, double locationY) {
        this.sensorId = sensorId;
        this.timestamp = LocalDateTime.now();
        this.vibrationLevel = vibrationLevel;
        this.temperature = temperature;
        this.moistureLevel = moistureLevel;
        this.pressure = pressure;
        this.locationX = locationX;
        this.locationY = locationY;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public double getVibrationLevel() { return vibrationLevel; }
    public void setVibrationLevel(double vibrationLevel) { this.vibrationLevel = vibrationLevel; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public double getMoistureLevel() { return moistureLevel; }
    public void setMoistureLevel(double moistureLevel) { this.moistureLevel = moistureLevel; }
    
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    
    public double getLocationX() { return locationX; }
    public void setLocationX(double locationX) { this.locationX = locationX; }
    
    public double getLocationY() { return locationY; }
    public void setLocationY(double locationY) { this.locationY = locationY; }
}

// RiskAssessment.java
package com.mining.rockfall.model;

import java.time.LocalDateTime;

public class RiskAssessment {
    
    public enum RiskLevel {
        LOW(0, "Safe conditions"),
        MEDIUM(1, "Monitor closely"),
        HIGH(2, "Evacuation recommended"),
        CRITICAL(3, "Immediate evacuation required");
        
        private final int value;
        private final String description;
        
        RiskLevel(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        public int getValue() { return value; }
        public String getDescription() { return description; }
        
        public static RiskLevel fromValue(int value) {
            for (RiskLevel level : values()) {
                if (level.value == value) return level;
            }
            return LOW;
        }
    }
    
    private RiskLevel riskLevel;
    private double confidenceScore;
    private String location;
    private LocalDateTime assessmentTime;
    private String[] contributingFactors;
    
    public RiskAssessment(RiskLevel riskLevel, double confidenceScore, String location) {
        this.riskLevel = riskLevel;
        this.confidenceScore = confidenceScore;
        this.location = location;
        this.assessmentTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getAssessmentTime() { return assessmentTime; }
    public void setAssessmentTime(LocalDateTime assessmentTime) { this.assessmentTime = assessmentTime; }
    
    public String[] getContributingFactors() { return contributingFactors; }
    public void setContributingFactors(String[] contributingFactors) { this.contributingFactors = contributingFactors; }
}

// ===== 3. CORE SERVICES =====

// RockfallPredictionService.java
package com.mining.rockfall.service;

import com.mining.rockfall.model.*;
import com.mining.rockfall.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Arrays;

@Service
public class RockfallPredictionService {
    
    private static final Logger logger = LoggerFactory.getLogger(RockfallPredictionService.class);
    
    @Autowired
    private SensorDataProcessor sensorProcessor;
    
    @Autowired
    private MachineLearningEngine mlEngine;
    
    @Autowired
    private AlertService alertService;
    
    @Autowired
    private SensorReadingRepository sensorRepository;
    
    public RiskAssessment predictRockfall(List<SensorReading> readings) {
        logger.info("Starting rockfall prediction for {} sensor readings", readings.size());
        
        try {
            // Process sensor data
            ProcessedSensorData processedData = sensorProcessor.processSensorData(readings);
            
            // Calculate risk using ML model
            RiskAssessment.RiskLevel riskLevel = mlEngine.predictRiskLevel(processedData);
            
            // Calculate confidence score
            double confidence = calculateConfidenceScore(processedData, riskLevel);
            
            // Determine location (using average of sensor locations)
            String location = determineRiskLocation(readings);
            
            // Create risk assessment
            RiskAssessment assessment = new RiskAssessment(riskLevel, confidence, location);
            assessment.setContributingFactors(identifyContributingFactors(processedData));
            
            // Trigger alerts if necessary
            if (riskLevel == RiskAssessment.RiskLevel.HIGH || 
                riskLevel == RiskAssessment.RiskLevel.CRITICAL) {
                alertService.triggerAlert(assessment);
            }
            
            logger.info("Prediction completed: Risk Level = {}, Confidence = {}", 
                       riskLevel, confidence);
            
            return assessment;
            
        } catch (Exception e) {
            logger.error("Error in rockfall prediction: ", e);
            return new RiskAssessment(RiskAssessment.RiskLevel.LOW, 0.0, "Unknown");
        }
    }
    
    private double calculateConfidenceScore(ProcessedSensorData data, 
                                          RiskAssessment.RiskLevel riskLevel) {
        // Simple confidence calculation based on data consistency
        double vibrationConsistency = data.getVibrationConsistency();
        double temperatureStability = data.getTemperatureStability();
        double dataQuality = data.getDataQualityScore();
        
        return (vibrationConsistency + temperatureStability + dataQuality) / 3.0;
    }
    
    private String determineRiskLocation(List<SensorReading> readings) {
        if (readings.isEmpty()) return "Unknown";
        
        double avgX = readings.stream().mapToDouble(SensorReading::getLocationX).average().orElse(0);
        double avgY = readings.stream().mapToDouble(SensorReading::getLocationY).average().orElse(0);
        
        return String.format("Sector %.1f,%.1f", avgX, avgY);
    }
    
    private String[] identifyContributingFactors(ProcessedSensorData data) {
        List<String> factors = Arrays.asList();
        
        if (data.getVibrationLevel() > 50) {
            factors.add("High vibration levels detected");
        }
        if (data.getTemperatureVariation() > 10) {
            factors.add("Significant temperature fluctuations");
        }
        if (data.getMoistureLevel() > 80) {
            factors.add("High moisture content");
        }
        if (data.getPressureChanges() > 5) {
            factors.add("Pressure variations observed");
        }
        
        return factors.toArray(new String[0]);
    }
}

// SensorDataProcessor.java
package com.mining.rockfall.service;

import com.mining.rockfall.model.SensorReading;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.DoubleSummaryStatistics;

@Service
public class SensorDataProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(SensorDataProcessor.class);
    
    public ProcessedSensorData processSensorData(List<SensorReading> readings) {
        logger.info("Processing {} sensor readings", readings.size());
        
        if (readings.isEmpty()) {
            return new ProcessedSensorData();
        }
        
        // Calculate statistics for vibration
        DoubleSummaryStatistics vibrationStats = readings.stream()
                .mapToDouble(SensorReading::getVibrationLevel)
                .summaryStatistics();
        
        // Calculate statistics for temperature
        DoubleSummaryStatistics temperatureStats = readings.stream()
                .mapToDouble(SensorReading::getTemperature)
                .summaryStatistics();
        
        // Calculate statistics for moisture
        DoubleSummaryStatistics moistureStats = readings.stream()
                .mapToDouble(SensorReading::getMoistureLevel)
                .summaryStatistics();
        
        // Calculate statistics for pressure
        DoubleSummaryStatistics pressureStats = readings.stream()
                .mapToDouble(SensorReading::getPressure)
                .summaryStatistics();
        
        ProcessedSensorData processedData = new ProcessedSensorData();
        
        // Set vibration data
        processedData.setVibrationLevel(vibrationStats.getAverage());
        processedData.setVibrationConsistency(calculateConsistency(vibrationStats));
        
        // Set temperature data
        processedData.setTemperatureLevel(temperatureStats.getAverage());
        processedData.setTemperatureVariation(temperatureStats.getMax() - temperatureStats.getMin());
        processedData.setTemperatureStability(calculateStability(temperatureStats));
        
        // Set moisture data
        processedData.setMoistureLevel(moistureStats.getAverage());
        
        // Set pressure data
        processedData.setPressureLevel(pressureStats.getAverage());
        processedData.setPressureChanges(pressureStats.getMax() - pressureStats.getMin());
        
        // Calculate overall data quality
        processedData.setDataQualityScore(calculateDataQuality(readings));
        
        logger.info("Data processing completed successfully");
        return processedData;
    }
    
    private double calculateConsistency(DoubleSummaryStatistics stats) {
        if (stats.getCount() == 0) return 0.0;
        
        double range = stats.getMax() - stats.getMin();
        double average = stats.getAverage();
        
        // Consistency is inversely related to coefficient of variation
        if (average == 0) return 1.0;
        return Math.max(0.0, 1.0 - (range / average));
    }
    
    private double calculateStability(DoubleSummaryStatistics stats) {
        if (stats.getCount() == 0) return 0.0;
        
        double range = stats.getMax() - stats.getMin();
        // Normalize stability score (smaller range = higher stability)
        return Math.max(0.0, 1.0 - (range / 100.0));
    }
    
    private double calculateDataQuality(List<SensorReading> readings) {
        // Simple data quality based on completeness and reasonableness
        int validReadings = 0;
        
        for (SensorReading reading : readings) {
            if (isValidReading(reading)) {
                validReadings++;
            }
        }
        
        return (double) validReadings / readings.size();
    }
    
    private boolean isValidReading(SensorReading reading) {
        // Check if reading values are within reasonable ranges
        return reading.getVibrationLevel() >= 0 && reading.getVibrationLevel() <= 1000 &&
               reading.getTemperature() >= -50 && reading.getTemperature() <= 100 &&
               reading.getMoistureLevel() >= 0 && reading.getMoistureLevel() <= 100 &&
               reading.getPressure() >= 0 && reading.getPressure() <= 200;
    }
}

// ProcessedSensorData.java (Data Transfer Object)
class ProcessedSensorData {
    private double vibrationLevel;
    private double vibrationConsistency;
    private double temperatureLevel;
    private double temperatureVariation;
    private double temperatureStability;
    private double moistureLevel;
    private double pressureLevel;
    private double pressureChanges;
    private double dataQualityScore;
    
    // Default constructor
    public ProcessedSensorData() {}
    
    // Getters and Setters
    public double getVibrationLevel() { return vibrationLevel; }
    public void setVibrationLevel(double vibrationLevel) { this.vibrationLevel = vibrationLevel; }
    
    public double getVibrationConsistency() { return vibrationConsistency; }
    public void setVibrationConsistency(double vibrationConsistency) { this.vibrationConsistency = vibrationConsistency; }
    
    public double getTemperatureLevel() { return temperatureLevel; }
    public void setTemperatureLevel(double temperatureLevel) { this.temperatureLevel = temperatureLevel; }
    
    public double getTemperatureVariation() { return temperatureVariation; }
    public void setTemperatureVariation(double temperatureVariation) { this.temperatureVariation = temperatureVariation; }
    
    public double getTemperatureStability() { return temperatureStability; }
    public void setTemperatureStability(double temperatureStability) { this.temperatureStability = temperatureStability; }
    
    public double getMoistureLevel() { return moistureLevel; }
    public void setMoistureLevel(double moistureLevel) { this.moistureLevel = moistureLevel; }
    
    public double getPressureLevel() { return pressureLevel; }
    public void setPressureLevel(double pressureLevel) { this.pressureLevel = pressureLevel; }
    
    public double getPressureChanges() { return pressureChanges; }
    public void setPressureChanges(double pressureChanges) { this.pressureChanges = pressureChanges; }
    
    public double getDataQualityScore() { return dataQualityScore; }
    public void setDataQualityScore(double dataQualityScore) { this.dataQualityScore = dataQualityScore; }
}

// MachineLearningEngine.java
package com.mining.rockfall.service;

import com.mining.rockfall.model.RiskAssessment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MachineLearningEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(MachineLearningEngine.class);
    
    public RiskAssessment.RiskLevel predictRiskLevel(ProcessedSensorData data) {
        logger.info("Predicting risk level using ML engine");
        
        // Simple rule-based classifier (you can replace with actual ML model)
        double riskScore = calculateRiskScore(data);
        
        logger.info("Calculated risk score: {}", riskScore);
        
        if (riskScore >= 0.8) {
            return RiskAssessment.RiskLevel.CRITICAL;
        } else if (riskScore >= 0.6) {
            return RiskAssessment.RiskLevel.HIGH;
        } else if (riskScore >= 0.4) {
            return RiskAssessment.RiskLevel.MEDIUM;
        } else {
            return RiskAssessment.RiskLevel.LOW;
        }
    }
    
    private double calculateRiskScore(ProcessedSensorData data) {
        double score = 0.0;
        
        // Vibration factor (30% weight)
        if (data.getVibrationLevel() > 70) {
            score += 0.3;
        } else if (data.getVibrationLevel() > 50) {
            score += 0.2;
        } else if (data.getVibrationLevel() > 30) {
            score += 0.1;
        }
        
        // Temperature factor (20% weight)
        if (data.getTemperatureVariation() > 15) {
            score += 0.2;
        } else if (data.getTemperatureVariation() > 10) {
            score += 0.15;
        } else if (data.getTemperatureVariation() > 5) {
            score += 0.1;
        }
        
        // Moisture factor (25% weight)
        if (data.getMoistureLevel() > 85) {
            score += 0.25;
        } else if (data.getMoistureLevel() > 70) {
            score += 0.2;
        } else if (data.getMoistureLevel() > 50) {
            score += 0.1;
        }
        
        // Pressure factor (25% weight)
        if (data.getPressureChanges() > 10) {
            score += 0.25;
        } else if (data.getPressureChanges() > 7) {
            score += 0.2;
        } else if (data.getPressureChanges() > 4) {
            score += 0.1;
        }
        
        // Data quality adjustment
        score *= data.getDataQualityScore();
        
        return Math.min(1.0, score); // Cap at 1.0
    }
}

// ===== 4. REST CONTROLLERS =====

// MonitoringController.java
package com.mining.rockfall.controller;

import com.mining.rockfall.model.*;
import com.mining.rockfall.service.RockfallPredictionService;
import com.mining.rockfall.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    @Autowired
    private RockfallPredictionService predictionService;
    
    @Autowired
    private SensorReadingRepository sensorRepository;
    
    @GetMapping("/current-status")
    public ResponseEntity<Map<String, Object>> getCurrentStatus() {
        // Get recent sensor readings (last 10 minutes)
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<SensorReading> recentReadings = sensorRepository.findByTimestampAfter(tenMinutesAgo);
        
        // Predict current risk
        RiskAssessment currentRisk = predictionService.predictRockfall(recentReadings);
        
        // Create response
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", LocalDateTime.now());
        status.put("riskLevel", currentRisk.getRiskLevel());
        status.put("confidence", currentRisk.getConfidenceScore());
        status.put("location", currentRisk.getLocation());
        status.put("totalSensors", recentReadings.size());
        status.put("activeSensors", recentReadings.stream().mapToLong(r -> 1).count());
        
        return ResponseEntity.ok(status);
    }
    
    @PostMapping("/sensor-data")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorReading reading) {
        // Save sensor reading
        reading.setTimestamp(LocalDateTime.now());
        sensorRepository.save(reading);
        
        return ResponseEntity.ok("Sensor data received successfully");
    }
    
    @GetMapping("/risk-assessment")
    public ResponseEntity<RiskAssessment> getRiskAssessment() {
        // Get last hour of data for comprehensive assessment
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<SensorReading> readings = sensorRepository.findByTimestampAfter(oneHourAgo);
        
        RiskAssessment assessment = predictionService.predictRockfall(readings);
        
        return ResponseEntity.ok(assessment);
    }
    
    @GetMapping("/sensor-readings/{sensorId}")
    public ResponseEntity<List<SensorReading>> getSensorReadings(
            @PathVariable String sensorId,
            @RequestParam(defaultValue = "24") int hours) {
        
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        List<SensorReading> readings = sensorRepository.findBySensorIdAndTimestampAfter(sensorId, startTime);
        
        return ResponseEntity.ok(readings);
    }
}

// ===== 5. DATA REPOSITORIES =====

// SensorReadingRepository.java
package com.mining.rockfall.repository;

import com.mining.rockfall.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    
    List<SensorReading> findByTimestampAfter(LocalDateTime timestamp);
    
    List<SensorReading> findBySensorIdAndTimestampAfter(String sensorId, LocalDateTime timestamp);
    
    @Query("SELECT DISTINCT r.sensorId FROM SensorReading r")
    List<String> findAllSensorIds();
    
    @Query("SELECT r FROM SensorReading r WHERE r.timestamp BETWEEN ?1 AND ?2 ORDER BY r.timestamp DESC")
    List<SensorReading> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

// ===== 6. ALERT SERVICE =====

// AlertService.java
package com.mining.rockfall.service;

import com.mining.rockfall.model.RiskAssessment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlertService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    
    public void triggerAlert(RiskAssessment assessment) {
        logger.warn("🚨 ROCKFALL ALERT TRIGGERED! Risk Level: {}, Location: {}", 
                   assessment.getRiskLevel(), assessment.getLocation());
        
        switch (assessment.getRiskLevel()) {
            case CRITICAL:
                triggerCriticalAlert(assessment);
                break;
            case HIGH:
                triggerHighRiskAlert(assessment);
                break;
            case MEDIUM:
                triggerMediumRiskAlert(assessment);
                break;
            default:
                logger.info("Low risk - no alert needed");
        }
    }
    
    private void triggerCriticalAlert(RiskAssessment assessment) {
        logger.error("🔴 CRITICAL ALERT: Immediate evacuation required at {}", 
                    assessment.getLocation());
        
        // In real implementation:
        // - Send SMS to all personnel
        // - Activate emergency sirens
        // - Notify emergency response team
        // - Log to incident management system
        
        sendNotification("CRITICAL", "Immediate evacuation required", assessment);
    }
    
    private void triggerHighRiskAlert(RiskAssessment assessment) {
        logger.warn("🟡 HIGH RISK ALERT: Evacuation recommended at {}", 
                   assessment.getLocation());
        
        sendNotification("HIGH", "Evacuation recommended", assessment);
    }
    
    private void triggerMediumRiskAlert(RiskAssessment assessment) {
        logger.info("🟨 MEDIUM RISK ALERT: Monitor closely at {}", 
                   assessment.getLocation());
        
        sendNotification("MEDIUM", "Enhanced monitoring required", assessment);
    }
    
    private void sendNotification(String alertLevel, String message, RiskAssessment assessment) {
        // Simulate sending notifications
        logger.info("📧 Sending {} alert: {} | Confidence: {:.2f} | Location: {}", 
                   alertLevel, message, assessment.getConfidenceScore(), assessment.getLocation());
        
        // In real implementation, integrate with:
        // - Email service (SendGrid, AWS SES)
        // - SMS service (Twilio, AWS SNS)
        // - Push notification service
        // - Slack/Teams webhook
    }
}

// ===== 7. CONFIGURATION FILES =====

// application.yml
/*
server:
  port: 8080
  servlet:
    context-path: /rockfall-api

spring:
  application:
    name: rockfall-prediction-system
  
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        
  h2:
    console:
      enabled: true
      path: /h2-console

logging:
  level:
    com.mining.rockfall: DEBUG
    org.springframework.web: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

rockfall:
  prediction:
    alert-threshold: 0.7
    data-retention-days: 30
  sensors:
    update-interval: 30 # seconds
    max-sensors: 50
*/

// pom.xml
/*
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.mining</groupId>
    <artifactId>rockfall-prediction-system</artifactId>
    <version>1.0.0</version>
    <name>Rockfall Prediction System</name>
    <description>AI-Based Rockfall Prediction and Alert System for Open-Pit Mines</description>
    
    <properties>
        <java.version>11</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Machine Learning (Weka) -->
        <dependency>
            <groupId>nz.ac.waikato.cms.weka</groupId>
            <artifactId>weka-stable</artifactId>
            <version>3.8.6</version>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- JSON Processing -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
        
        <!-- Apache Commons Math for statistical calculations -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-math3</artifactId>
            <version>3.6.1</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
*/

// ===== 8. DATA SIMULATION SERVICE =====

// SensorDataSimulator.java
package com.mining.rockfall.service;

import com.mining.rockfall.model.SensorReading;
import com.mining.rockfall.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class SensorDataSimulator {
    
    private static final Logger logger = LoggerFactory.getLogger(SensorDataSimulator.class);
    private final Random random = new Random();
    
    @Autowired
    private SensorReadingRepository sensorRepository;
    
    private final List<String> sensorIds = Arrays.asList(
        "SENSOR_001", "SENSOR_002", "SENSOR_003", "SENSOR_004", "SENSOR_005",
        "SENSOR_006", "SENSOR_007", "SENSOR_008", "SENSOR_009", "SENSOR_010"
    );
    
    // Simulate sensor data every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void generateSensorData() {
        logger.info("Generating simulated sensor data...");
        
        for (String sensorId : sensorIds) {
            SensorReading reading = generateRandomReading(sensorId);
            sensorRepository.save(reading);
        }
        
        logger.info("Generated data for {} sensors", sensorIds.size());
    }
    
    private SensorReading generateRandomReading(String sensorId) {
        // Generate realistic but random sensor data
        double vibrationLevel = generateVibrationLevel();
        double temperature = generateTemperature();
        double moistureLevel = generateMoistureLevel();
        double pressure = generatePressure();
        double[] location = getSensorLocation(sensorId);
        
        SensorReading reading = new SensorReading(
            sensorId,
            vibrationLevel,
            temperature,
            moistureLevel,
            pressure,
            location[0],
            location[1]
        );
        
        // Occasionally generate "concerning" readings for testing alerts
        if (random.nextDouble() < 0.05) { // 5% chance
            reading = generateHighRiskReading(sensorId, location);
            logger.warn("Generated HIGH RISK reading for sensor {}", sensorId);
        }
        
        return reading;
    }
    
    private double generateVibrationLevel() {
        // Normal range: 10-40 Hz, with occasional spikes
        double baseLevel = 10 + random.nextGaussian() * 8;
        return Math.max(0, Math.min(100, baseLevel));
    }
    
    private double generateTemperature() {
        // Normal mining temperature range: 15-35°C
        double baseTemp = 25 + random.nextGaussian() * 5;
        return Math.max(-10, Math.min(60, baseTemp));
    }
    
    private double generateMoistureLevel() {
        // Normal moisture range: 30-70%
        double baseMoisture = 50 + random.nextGaussian() * 15;
        return Math.max(0, Math.min(100, baseMoisture));
    }
    
    private double generatePressure() {
        // Normal pressure range: 95-105 kPa
        double basePressure = 100 + random.nextGaussian() * 3;
        return Math.max(80, Math.min(120, basePressure));
    }
    
    private double[] getSensorLocation(String sensorId) {
        // Assign fixed locations to sensors (simulating mine layout)
        int sensorIndex = Integer.parseInt(sensorId.substring(sensorId.length() - 3));
        double x = (sensorIndex % 5) * 100.0; // Grid layout
        double y = (sensorIndex / 5) * 100.0;
        return new double[]{x, y};
    }
    
    private SensorReading generateHighRiskReading(String sensorId, double[] location) {