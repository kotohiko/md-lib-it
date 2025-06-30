# 1   认识工作流

## 1.1   概念



## 1.2   工作流系统



## 1.3   适用行业





## 1.4   具体应用



## 1.5   实现方式



# 2   Activiti 7 概述

## 2.1   简介

### 2.1.1   BPM

**BPM（Business Process Management）**，中文翻译为「业务流程管理」，是一种规范化的构造端到端的业务流程，用以持续地提高组织业务效率。

### 2.1.2   BPM 软件



### 2.1.3   BPMN



## 2.2   使用步骤

###  部署

Activiti 作为一个工作流引擎，在 IT 的视角就是一系列 JAR 包 API，业务系统通过访问 Activiti 的接口，就可以方便地操作流程相关数据，这样就可以把工作流环境与业务系统的环境集成在一起。

### 流程定义

使用 Activiti 流程建模工具（activiti-designer）定义业务流程：.bpmn 文件。

### 流程定义部署



### 启动一个流程实例

流程实例就是 ProcessInstance。启动一个流程实例意味着一次业务流程的运行。

### 用户查询待办任务（Task）

流程启动以后，就意味着系统的业务流程已经交给 Activiti 管理，通过 Activiti 就可以查询当前流程执行到哪里了。当前用户需要处理什么任务，也就不需要程序员再去编写 SQL 查询了。

### 用户处理任务

用户查询到待办任务后，就可以处理某个任务。如果这个任务处理完成，还需要转下一个人处理，这个过程也是 Activiti 来管理的。









# 3   环境搭建



## 3.3   数据库支持

工作流本身就是一个异步、实时性弱的业务场景。比如员工发起了一个申请，领导事情比较多，经常可能不会迅速处理，有的时候甚至几个星期后才处理。所以工作流几乎硬性需要持久层的支持。

| 数据库类型 | 版本 | JDBC 连接示例 | 说明 |
| ---------- | ---- | ------------- | ---- |
|            |      |               |      |
|            |      |               |      |
|            |      |               |      |
|            |      |               |      |
|            |      |               |      |
|            |      |               |      |

Activiti 默认情况下会生成 25 张表来完成它的工作，表名如下：

| 分类                                                     | 表名                  | 描述                                                         |
| -------------------------------------------------------- | --------------------- | ------------------------------------------------------------ |
|                                                          | act_evt_log           |                                                              |
| GE 表示 general，表示存储通用数据                        | act_ge_bytearray      | 存储二进制数据，这些数据通常与流程实例相关联                 |
|                                                          | act_ge_property       | 系统相关属性                                                 |
| HI 表示 history，表示存储历史数据，如审批流程记录        | act_hi_actinst        | 记录流程实例中每个活动的执行情况，包括活动的开始、完成和执行结果 |
|                                                          | act_hi_attachment     | 历史流程附件                                                 |
|                                                          | act_hi_comment        | 历史说明性信息                                               |
|                                                          | act_hi_detail         | 历史流程运行中的细节信息                                     |
|                                                          | act_hi_identitylink   | 历史流程运行过程中的用户关系                                 |
|                                                          | act_hi_procinst       | 流程实例的总体信息，包括流程的启动、执行和完成情况           |
|                                                          | act_hi_taskinst       | 历史任务实例                                                 |
|                                                          | act_hi_varinst        | 历史流程运行中的变量信息                                     |
|                                                          | act_procdef_info      |                                                              |
| RE 表示 repository，表示存储流程定义的内容、以及静态资源 | act_re_deployment     | 部署单元信息                                                 |
|                                                          | act_re_model          | 流程模型（BPMN 2.0 文件）的相关信息                          |
|                                                          | act_re_procdef        |                                                              |
| RU 表示 runtime，表示运行时                              | act_ru_deadletter_job |                                                              |
|                                                          | act_ru_event_subscr   |                                                              |
|                                                          | act_ru_execution      |                                                              |
|                                                          | act_ru_identitylink   |                                                              |
|                                                          | act_ru_integration    |                                                              |
|                                                          | act_ru_job            | 运行时作业                                                   |
|                                                          | act_ru_suspended_job  |                                                              |
|                                                          | act_ru_task           | 运行时任务                                                   |
|                                                          | act_ru_timer_job      |                                                              |
|                                                          | act_ru_variable       | 运行时变量                                                   |





# 4   类关系图



## 4.2   activiti.cfg.xml



## 4.3   流程引擎配置类



## 4.4   工作流引擎创建

工作流引擎（process engine）相当于一个门面接口



## 4.5   Service 服务接口

Service 是工作流引擎用于进行工作流部署、执行、管理的服务接口，我们使用这些接口就是操作服务对应的数据表。

### 4.5.1   Service 创建方式

通过 `ProcessEngine` 可以创建 Service。

```java
RuntimeService runtimeService = processEngine.getRuntimeService();
RepositoryService repositoryService = processEngine.getRepositoryService();
TaskService taskService = processEngine.getTaskService();
```

### 4.5.2   Service 一览

| 名称                | 作用         |
| ------------------- | ------------ |
| `RepositoryService` | 资源管理     |
| `RuntimeService`    | 流程运行管理 |
| `TaskService`       | 任务管理     |
| `HistoryService`    | 历史管理     |
| `ManagementService` | 引擎管理     |

#### RepositoryService



#### RuntimeService



#### TaskService



#### HistoryService











# 5   入门







# 6   流程操作