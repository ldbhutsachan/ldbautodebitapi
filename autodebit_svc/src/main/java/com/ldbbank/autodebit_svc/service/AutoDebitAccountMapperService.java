package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountMapperEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitAccountEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.AutoDebitCompanyEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.SectionEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.UserDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountMapperRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitAccountRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.AutoDebitCompanyRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.SectionRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.UserDbRepository;
import com.ldbbank.autodebit_svc.model.AutoDebitAccountMapperDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AutoDebitAccountMapperService {

    private final AutoDebitAccountMapperRepository mapperRepo;
    private final AutoDebitAccountRepository accountRepo;
    private final AutoDebitCompanyRepository companyRepo;
    private final SectionRepository sectionRepo;
    private final UserDbRepository userRepo;

    public AutoDebitAccountMapperService(AutoDebitAccountMapperRepository mapperRepo,
                                         AutoDebitAccountRepository accountRepo,
                                         AutoDebitCompanyRepository companyRepo,
                                         SectionRepository sectionRepo,
                                         UserDbRepository userRepo) {
        this.mapperRepo = mapperRepo;
        this.accountRepo = accountRepo;
        this.companyRepo = companyRepo;
        this.sectionRepo = sectionRepo;
        this.userRepo = userRepo;
    }

    public AutoDebitAccountMapperEntity save(AutoDebitAccountMapperEntity e) {
        LocalDateTime now = LocalDateTime.now();
        e.setCreatedAt(now);
        e.setUpdatedAt(now);
        if (e.getStatus() == null) e.setStatus(1);
        return mapperRepo.save(e);
    }

    public Optional<AutoDebitAccountMapperEntity> update(Long id, AutoDebitAccountMapperEntity change) {
        return mapperRepo.findById(id).map(existing -> {
            if (change.getAccountId() != null) existing.setAccountId(change.getAccountId());
            if (change.getCompanyId() != null) existing.setCompanyId(change.getCompanyId());
            if (change.getSectionNo() != null) existing.setSectionNo(change.getSectionNo());
            if (change.getUserId() != null) existing.setUserId(change.getUserId());
            if (change.getStatus() != null) existing.setStatus(change.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return mapperRepo.save(existing);
        });
    }

    public boolean updateStatus(Long id, Integer status) {
        return mapperRepo.findById(id).map(existing -> {
            existing.setStatus(status);
            existing.setUpdatedAt(LocalDateTime.now());
            mapperRepo.save(existing);
            return true;
        }).orElse(false);
    }

    public List<AutoDebitAccountMapperDto> findByStatusWithJoin(Integer status) {
        List<AutoDebitAccountMapperEntity> maps = mapperRepo.findByStatus(status);
        List<AutoDebitAccountMapperDto> result = new ArrayList<>();
        for (AutoDebitAccountMapperEntity m : maps) {
            AutoDebitAccountMapperDto d = mapToDto(m);
            // account
            if (m.getAccountId() != null) {
                Optional<AutoDebitAccountEntity> a = accountRepo.findById(m.getAccountId());
                a.ifPresent(account -> {
                    d.setAccountNo(account.getAccountNo());
                    d.setAccountName(account.getAccountName());
                });
            }
            // company
            if (m.getCompanyId() != null) {
                Optional<AutoDebitCompanyEntity> c = companyRepo.findById(m.getCompanyId());
                c.ifPresent(company -> {
                    d.setCompanyCode(company.getCompanyCode());
                    d.setCompanyName(company.getCompanyName());
                });
            }
            // section
            if (m.getSectionNo() != null) {
                Optional<SectionEntity> s = sectionRepo.findBySectionNo(m.getSectionNo());
                s.ifPresent(section -> d.setSectionName(section.getSectionName()));
            }
            // user
            if (m.getUserId() != null) {
                Optional<UserDbEntity> u = userRepo.findById(m.getUserId());
                u.ifPresent(user -> d.setUserName(user.getUserName()));
            }
            result.add(d);
        }
        return result;
    }

    private AutoDebitAccountMapperDto mapToDto(AutoDebitAccountMapperEntity m) {
        AutoDebitAccountMapperDto d = new AutoDebitAccountMapperDto();
        d.setId(m.getId());
        d.setAccountId(m.getAccountId());
        d.setCompanyId(m.getCompanyId());
        d.setSectionNo(m.getSectionNo());
        d.setUserId(m.getUserId());
        d.setStatus(m.getStatus());
        d.setCreatedAt(m.getCreatedAt());
        d.setUpdatedAt(m.getUpdatedAt());
        return d;
    }
}
