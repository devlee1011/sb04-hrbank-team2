# 2팀
[팀 협업 문서](https://www.notion.so/2-22a2c93d1bbc80afaa3afb961f737b9b)

## 팀원 구성
+ 이지현 ([Github]())
+ 김민수 ([Githb]())
+ 오명재 ([Githb]())

---

## 프로젝트 소개
- EMS(Employee Management System) - 직원 관리 시스템 HrBank
- 프로젝트 기간 : 2025.07.28 ~ 2025.08.06

---

## 기술 스택
- 사용 언어 : Java
- BackendFramework: Spring Boot, Spring Data JPA, MapStruct
- Database: PostgreSQL
- 배포 플랫폼 : Railway
- 공통 Tool: Git & Github, Discord

---

## 팀원 별 구현 기능 상세
### 이지현
-
-
-

### 김민수
-
-
-

### 오명재
-
-
-

---

## 파일구조
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── codeit
│   │           └── hrbank
│   │               ├── HrBankApplication.java
│   │               ├── backup
│   │               │   ├── controller
│   │               │   │   └── BackupController.java
│   │               │   ├── dto
│   │               │   │   ├── BackupDto.java
│   │               │   │   ├── request
│   │               │   │   │   └── BackupGetAllRequest.java
│   │               │   │   └── response
│   │               │   │       └── CursorPageResponseBackupDto.java
│   │               │   ├── entitiy
│   │               │   │   ├── Backup.java
│   │               │   │   └── BackupStatus.java
│   │               │   ├── mapper
│   │               │   │   └── BackupMapper.java
│   │               │   ├── repository
│   │               │   │   └── BackupRepository.java
│   │               │   ├── scheduler
│   │               │   │   └── BackupScheduler.java
│   │               │   ├── service
│   │               │   │   ├── BackupService.java
│   │               │   │   └── BasicBackupService.java
│   │               │   └── specification
│   │               │       └── BackupSpecification.java
│   │               ├── base_entity
│   │               │   ├── BaseEntity.java
│   │               │   └── BaseUpdatableEntity.java
│   │               ├── change_log
│   │               │   ├── controller
│   │               │   │   └── ChangeLogController.java
│   │               │   ├── dto
│   │               │   │   ├── ChangeLogDto.java
│   │               │   │   ├── DiffDto.java
│   │               │   │   ├── request
│   │               │   │   │   └── ChangeLogGetAllRequest.java
│   │               │   │   └── response
│   │               │   │       └── CursorPageResponseChangeLogDto.java
│   │               │   ├── entity
│   │               │   │   ├── ChangeLog.java
│   │               │   │   ├── ChangeLogDetail.java
│   │               │   │   └── ChangeLogType.java
│   │               │   ├── mapper
│   │               │   │   └── ChangeLogMapper.java
│   │               │   ├── repository
│   │               │   │   ├── ChangeLogDetailRepository.java
│   │               │   │   └── ChangeLogRepository.java
│   │               │   ├── service
│   │               │   │   ├── BasicChangeLogService.java
│   │               │   │   └── ChangeLogService.java
│   │               │   └── specification
│   │               │       └── ChangeLogSpecification.java
│   │               ├── department
│   │               │   ├── controller
│   │               │   │   └── DepartmentController.java
│   │               │   ├── dto
│   │               │   │   ├── request
│   │               │   │   │   ├── DepartmentCreateRequest.java
│   │               │   │   │   ├── DepartmentGetAllRequest.java
│   │               │   │   │   └── DepartmentUpdateRequest.java
│   │               │   │   └── response
│   │               │   │       ├── CursorPageResponseDepartmentDto.java
│   │               │   │       └── DepartmentDto.java
│   │               │   ├── entity
│   │               │   │   └── Department.java
│   │               │   ├── mapper
│   │               │   │   └── DepartmentMapper.java
│   │               │   ├── repository
│   │               │   │   └── DepartmentRepository.java
│   │               │   ├── service
│   │               │   │   ├── BasicDepartmentService.java
│   │               │   │   └── DepartmentService.java
│   │               │   └── specification
│   │               │       └── DepartmentSpecification.java
│   │               ├── employee
│   │               │   ├── controller
│   │               │   │   └── EmployeeController.java
│   │               │   ├── dto
│   │               │   │   ├── CursorPageResponseEmployeeDto.java
│   │               │   │   ├── EmployeeDistributionDto.java
│   │               │   │   ├── EmployeeDto.java
│   │               │   │   ├── EmployeeTrendDto.java
│   │               │   │   └── request
│   │               │   │       ├── EmployeeCreateRequest.java
│   │               │   │       ├── EmployeeGetAllRequest.java
│   │               │   │       └── EmployeeUpdateRequest.java
│   │               │   ├── entity
│   │               │   │   ├── Employee.java
│   │               │   │   ├── EmployeeStatus.java
│   │               │   │   └── UnitType.java
│   │               │   ├── mapper
│   │               │   │   └── EmployeeMapper.java
│   │               │   ├── projection
│   │               │   │   ├── EmployeeCountByDepartmentProjection.java
│   │               │   │   ├── EmployeeDistributionProjection.java
│   │               │   │   └── EmployeeTrendProjection.java
│   │               │   ├── repository
│   │               │   │   └── EmployeeRepository.java
│   │               │   ├── service
│   │               │   │   ├── BasicEmployeeService.java
│   │               │   │   └── EmployeeService.java
│   │               │   ├── specification
│   │               │   │   └── EmployeeSpecification.java
│   │               │   └── utility
│   │               │       └── HireDatePeriod.java
│   │               ├── event
│   │               │   ├── EmployeeLogEvent.java
│   │               │   └── handler
│   │               │       └── EmployeeChangeLogEventHandler.java
│   │               ├── exception
│   │               │   ├── BusinessLogicException.java
│   │               │   ├── ErrorResponse.java
│   │               │   ├── ExceptionCode.java
│   │               │   └── GlobalExceptionHandler.java
│   │               └── stored_file
│   │                   ├── controller
│   │                   │   └── StoredFileController.java
│   │                   ├── dto
│   │                   │   ├── request
│   │                   │   └── response
│   │                   ├── entity
│   │                   │   └── StoredFile.java
│   │                   ├── mapper
│   │                   ├── repository
│   │                   │   └── StoredFileRepository.java
│   │                   └── service
│   │                       ├── BasicStoredFileService.java
│   │                       ├── LocalStoredFileStorage.java
│   │                       └── StoredFileService.java
│   └── resources
│       ├── README.md
│       ├── application.yaml
│       ├── schema.sql
│       ├── static
│       │   ├── assets
│       │   │   ├── images
│       │   │   │   └── default-profile.svg
│       │   │   └── index-aNksrdbr.js
│       │   ├── favicon.ico
│       │   └── index.html
│       └── templates
└── test
    └── java
        └── com
            └── codeit
                └── hrbank
                    └── HrBankApplicationTests.java
```

---

## 구현 홈페이지
### [구현 홈페이지](https://sb04-hrbank-team2-production.up.railway.app/)

---

## 프로젝트 회고록
### [발표자료](https://www.canva.com/design/DAGubVUuAYk/tQ9MHpwNq1YMW7voIzkXUQ/edit?utm_content=DAGubVUuAYk&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
