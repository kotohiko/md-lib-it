# 基础篇



## 1   工作流介绍

**工作流（Workflow）**，就是通过计算机对业务流程进行自动化管理。它主要解决的是：使在多个参与者之间按照某种预定义的规则自动进行传递文档、信息或任务的过程，从而实现某个预期的业务目标，或者促使此目标的实现。



## 2   Activiti

### 2.1   简介

Alfresco 软件在 2010 年 5 月 17 日宣布 Activiti 业务流程管理（BPM）开源项目的正式启动，其首席架构师由业务流程管理 BPM 的专家 Tom Baeyens 担任， Tom Baeyens 就是原来 jbpm 的架构师，而 jbpm 是一个非常有名的工作流引擎，当然 Activiti 也是一个工作流引擎。

Activiti 是一个工作流引擎，Activiti 可以将业务系统中复杂的业务流程抽取出来，使用专门的建模语言  BPMN 2.0 进行定义，业务流程按照预先定义的流程进行执行，实现了系统的流程由 Activiti 进行管理，减少业务系统由于流程变更进行系统升级改造的工作量，从而提高系统的健壮性，同时也减少了系统开发维护成本。

官方网站： [https](https://www.activiti.org/)[://](https://www.activiti.org/)[www](https://www.activiti.org/)[.](https://www.activiti.org/)[activiti](https://www.activiti.org/)[.](https://www.activiti.org/)[org](https://www.activiti.org/)[/](https://www.activiti.org/)

#### 2.1.1   BPM

#### 2.1.2   BPM 软件



### 2.2   使用步骤

1.  部署 Activiti

    Activiti 作为一种工作流引擎，其实就是一堆 jar 包 API。业务系统通过访问 Activiti 的接口，就可以便捷地操作流程相关数据，如此一来就可以把工作流环境与业务系统的环境集成在一起。

2.  流程定义

    使用 Activiti 流程建模工具（activity-designer）定义业务流程 .bpmn 文件。.bpmn 文件就是业务流程定义文件，通过 XML 定义业务流程。

3.  流程定义部署

    Activiti 部署业务流程定义（.bpmn文件）。

    使用 Activiti 提供的 API 把流程定义内容存储起来，在 Activiti 执行过程中可以查询定义的内容，Activiti 执行把流程定义内容存储在数据库中

4.  启动一个流程实例

    流程实例也叫：ProcessInstance

    启动一个流程实例表示一轮业务流程的运行的开始。

    在员工请假流程定义部署完成后，如果 A 要请假就可以启动一个流程实例，如果 B 要请假也启动一 个流程实例，两个流程的执行互相不影响。

5.  用户查询待办任务（task）

    系统的业务流程已经交给 Activiti 管理，通过 Activiti 就可以查询当前流程执行到哪了。当前用户需要办理什么任务，也是交给 Activiti 管理了，不需要开发人员再编写 SQL 语句查询。

6.  用户处理任务

    用户查询到待办任务后，就可以办理某个任务。如果这个任务办理完成还需要其它用户办理，比如采购单。创建后由部门经理审核，这个过程也是由 Activiti 帮我们完成了。

7.  流程结束











# 进阶篇



# 整合篇