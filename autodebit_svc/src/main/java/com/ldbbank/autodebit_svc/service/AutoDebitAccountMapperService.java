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
import com.ldbbank.autodebit_svc.model.AutoDebitAccountMapperReq;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public AutoDebitAccountMapperEntity save(AutoDebitAccountMapperReq req) {
        AutoDebitAccountMapperEntity entity = new AutoDebitAccountMapperEntity();

        // Map fields from request to entity
        entity.setFromAcctNo(req.getFromAcctNo());
        entity.setFromAcctName(req.getFromAcctName());
        entity.setFromAcctCcy(req.getFromAcctCcy());
        entity.setFromAcctType(req.getFromAcctType());
        entity.setToAcctNo(req.getToAcctNo());
        entity.setToAcctName(req.getToAcctName());
        entity.setToAcctCcy(req.getToAcctCcy());
        entity.setRemark(req.getRemark());
        entity.setUserBy(req.getUserBy());
        entity.setUserDate(req.getUserDate());
        entity.setStatus(req.getStatus());
        entity.setPartnerName(req.getPartnerName());
        entity.setBranchCode(req.getBranchCode());

        // Optional: set defaults for fields not in request
        entity.setUserEditing(null);
        entity.setEditingDate(null);
        entity.setUserStatusBy(null);
        entity.setUserStatusDate(null);
        entity.setSignature(null);

        // Save entity
        return mapperRepo.save(entity);
    }

    public Optional<AutoDebitAccountMapperEntity> update(Long id, AutoDebitAccountMapperEntity change) {
        return mapperRepo.findById(id).map(existing -> {
            if (change.getFromAcctNo() != null) existing.setFromAcctNo(change.getFromAcctNo());
            if (change.getPartnerName() != null) existing.setPartnerName(change.getPartnerName());
            if (change.getBranchCode() != null) existing.setBranchCode(change.getBranchCode());
            if (change.getUserBy() != null) existing.setUserBy(change.getUserBy());
            if (change.getStatus() != null) existing.setStatus(change.getStatus());
            existing.setEditingDate(LocalDate.from(LocalDateTime.now()));
            return mapperRepo.save(existing);
        });
    }

    public boolean updateStatus(Long id, Integer status) {
        return mapperRepo.findById(id).map(existing -> {
            existing.setStatus(status);
            existing.setEditingDate(LocalDate.from(LocalDateTime.now()));
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
            if (m.getFromAcctNo() != null) {
                Optional<AutoDebitAccountEntity> a = accountRepo.findByPartNerName(m.getPartnerName());
                a.ifPresent(account -> {
                    d.setAccountNo(account.getAccountNo());
                    d.setAccountName(account.getAccountName());
                });
            }
            // company
            if (m.getPartnerName() != null) {
                Optional<AutoDebitCompanyEntity> c = companyRepo.findById(Long.valueOf(m.getPartnerName()));
                c.ifPresent(company -> {
                    d.setCompanyCode(company.getCompanyCode());
                    d.setCompanyName(company.getCompanyName());
                });
            }
            // section
            if (m.getBranchCode() != null) {
                Optional<SectionEntity> s = sectionRepo.findBySectionNo(m.getBranchCode());
                s.ifPresent(section -> d.setSectionName(section.getSectionName()));
            }
            // user
            if (m.getUserBy() != null) {
                Optional<UserDbEntity> u = userRepo.findByUserName(m.getUserBy());
                u.ifPresent(user -> d.setUserName(user.getUserName()));
            }
            result.add(d);
        }
        return result;
    }

    private AutoDebitAccountMapperDto mapToDto(AutoDebitAccountMapperEntity m) {
        AutoDebitAccountMapperDto d = new AutoDebitAccountMapperDto();
        d.setId(m.getKeyId());
        d.setAccountNo(m.getFromAcctNo());
        d.setAccountName(m.getFromAcctName());
        d.setSectionNo(m.getBranchCode());
        d.setUserId(m.getUserBy());
        d.setStatus(m.getStatus());
        d.setCreatedAt(m.getUserDate());
        d.setUpdatedAt(m.getEditingDate());
        return d;
    }
}
