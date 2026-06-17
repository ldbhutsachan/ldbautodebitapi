package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AutoDebitCompanyService {
    private final AutoDebitCompanyRepository repo;

    public AutoDebitCompanyService(AutoDebitCompanyRepository repo) {
        this.repo = repo;
    }

    public AutoDebitCompanyEntity save(AutoDebitCompanyEntity e) {
        e.setCreatedAt(new Date());
        e.setUpdatedAt(new Date());
        if (e.getStatus() == null) e.setStatus("open");
        return repo.save(e);
    }

    public Optional<AutoDebitCompanyEntity> update(Long id, AutoDebitCompanyEntity change) {
        return repo.findById(id).map(existing -> {
            if (change.getCompanyCode() != null) {
                existing.setCompanyCode(change.getCompanyCode());
            }
            if (change.getCompanyName() != null) {
                existing.setCompanyName(change.getCompanyName());
            }
            if (change.getStatus() != null) {
                existing.setStatus(change.getStatus());
            }
            if (change.getImagePath() != null) {
                existing.setImagePath(change.getImagePath());
            }
            if (change.getImageName() != null) {
                existing.setImageName(change.getImageName());
            }
            if (change.getBatRunningDate() != null) {
                existing.setBatRunningDate(change.getBatRunningDate());
            }

            existing.setUpdatedAt(new Date());

            return repo.save(existing);
        });
    }

    public boolean disable(Long id) {
        return repo.findById(id).map(existing -> {
            existing.setStatus("close");
            existing.setUpdatedAt(new Date());
            repo.save(existing);
            return true;
        }).orElse(false);
    }

    public List<AutoDebitCompanyEntity> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    public List<AutoDebitCompanyEntity> findAll() {
        return repo.findAll();
    }

    public boolean updateImage(Long id, String imagePath, String imageName) {
        return repo.findById(id).map(existing -> {
            existing.setImagePath(imagePath);
            existing.setImageName(imageName);
            existing.setUpdatedAt(new Date());
            repo.save(existing);
            return true;
        }).orElse(false);
    }
}
