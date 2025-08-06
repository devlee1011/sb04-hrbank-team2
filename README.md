# 2팀
[팀 협업 문서](https://www.notion.so/2-22a2c93d1bbc80afaa3afb961f737b9b)

## 팀원 구성
+ 이지현 ([Github](https://github.com/devlee1011))
+ 김민수 ([Githb](https://github.com/NanHangBok))
+ 오명재 ([Githb](https://github.com/Oh-Myeongjae))

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
- 부서 관리 API
  - 부서 정보를 담은 POST 요청을 사용하여 부서를 등록하는 RESTful한 API 구현
  - GET 요청에 Id값 @PathVariable을 이용하여 해당 ID에 해당하는 부서의 상세정보를 불러오는 API 구현
  - PATCH 및 DELETE 요청을 사용하여 부서 정보 수정 및 삭제 API 구현
  - GET 요청에 쿼리파라미터를 사용하여 조건에 맞는 부서를 페이지네이션하여 목록을 조회하는 API 구현 (커서 기반)
    - 필터 조건 : 부서명 또는 설명 검색 모든 필터는 부분 일치 조건
    - 정렬 조건 : 부서명 또는 설립일로 오름차순 및 내림차순


- 대시보드
  - 대시보드 화면에 출력되는 직원 수 조회, 직원 분포 조회, 직원 수 추이 조회 기능 구현
  - 직원 수 조회
    - 상태 필터링 및 입사일 기간 필터링 기능 구현
  - 직원 분포 조회
    - 그룹화 기준 : 부서별, 직무별
  - 직원 수 추이 조회
    - 지정된 기간 및 시간 단위로 그룹화 기능 구현
      - 일, 주, 월, 분기, 년 단위로 추이 조회 기능 구현

### 김민수
- 직원 관리 API
  - 직원 정보를 담은 POST 요청을 사용하여 직원을 등록하는 RESTful한 API 구현
  - GET 요청에 Id값 @PathVariable을 이용하여 해당 ID 직원의 상세정보를 불러오는 API 구현
  - PATCH 및 DELETE 요청을 사용하여 직원 정보 수정 및 삭제 API 구현
  - GET 요청에 쿼리파라미터를 사용하여 조건에 맞는 직원을 페이지네이션하여 목록을 조회하는 API 구현 (커서 기반)
    - 필터 조건 :
      - 부분 일치 조건 : 이메일 또는 이름, 부서, 직함, 사원번호
      - 입사일(범위 조건), 상태(완전 일치 조건)
      - 여러 개의 조건 시 모든 조건을 만족하는 결과 조회 기능 구현
    - 정렬 조건 : 이름 또는 입사일 또는 사원번호 오름차순 및 내림차순
   

- 직원 정보 수정 이력 관리 API
  - 직원의 생성, 수정, 삭제 시 ApplicationEventPublisher 를 통해 작업이 완료되었을 경우에만 수정 이력 생성 기능 구현
  - GET 요청에 쿼리파라미터를 사용하여 조건에 맞는 수정 이력을 페이지네이션하여 목록을 조회하는 API 구현 (커서 기반)
    - 필터 조건 :
      - 부분 일치 조건 : 대상 직원 사번, 메모, IP 주소
      - 시간(범위 조건), 유형(완전 일치 조건)
      - 여러 개의 조건 시 모든 조건을 만족하는 결과 조회 기능 구현
    - 정렬 조건 : IP 주소 또는 시간 오름차순 및 내림차순
  - GET 요청에 Id값 @PathVariable을 이용하여 해당 ID 수정 이력의 상세정보를 불러오는 API 구현

### 오명재
- 데이터 백업 관리 API
  - POST 요청을 사용하여 데이터 백업을 진행하는 API 구현
    - 기존 백업과 현재 백업의 수정사항을 파악하여 수정되지 않았다면 저장하지 않도록 기능 구현
    - 배치처리를 통해 1시간마다 백업 확인 기능 구현
      - 백업 필요 시 백업 진행
    - 트랜잭션을 사용하여 작업의 성공 여부에 따라 다른 결과를 제출하도록 기능 구현
  - GET 요청에 쿼리파라미터를 사용하여 조건에 맞는 부서를 페이지네이션하여 목록을 조회하는 API 구현 (커서 기반)
    - 필터 조건 : 작업자(부분 일치 조건), 시작 시간(범위 조건), 상태(완전 일치 조건)
    - 정렬 조건 : 시작시간 또는 종료 시간 오름차순 또는 내림차순
  - GET 요청을 사용하여 지정된 상태에 가장 최근 백업 정보 조회 기능 구현
    - 성공, 실패, 진행 중 상태를 쿼리 파라미터로 받도록 API 엔드포인트 구현
   

- 파일 관리 기능 구현
  - DB 부하를 줄이기 위해 실제 파일은 로컬 디스크에 저장하도록 구현
  - DB에는 메타 정보만 담도록 구현
  - GET 요청에 ID를 담은 @PathVariable을 이용하여 실제 파일을 다운로드 하는 기능 구현

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
