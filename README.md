# Wholesale MongoDB

This is the Wholesale project implemented with MongoDB. It is part of the requirements for the module [CS4224 Distributed Databases](https://nusmods.com/modules/CS4224/distributed-databases) at the [National University of Singapore](http://www.nus.edu.sg).

[This repository](https://github.com/yunpengn/Wholesale-MongoDB) presents our approach to the project. Our team consists of

- [Niu Yunpeng](https://github.com/yunpengn)
- [Wang Junming](https://github.com/junming403)
- [Xiang Hailin](https://github.com/Hailinx)

## Setup Development Environment

- Install the latest version of [IntelliJ IDEA](https://www.jetbrains.com/idea/).
- Clone the repository by `git clone git@github.com:yunpengn/Wholesale-MongoDB.git`.
- Click `Import Project` and select `build.gradle`.
- Wait for Gradle to complete the setup.

## Deployment

- Install MongoDB, clone GitHub repository & download input data by `./scripts/setup.sh setup`.
    - If you have run this script previously on the same set of servers, kill all MongoDB instances by `./scripts/setup.sh force_kill_all`.
- Start new MongoDB instances and create cluster by `./scripts/setup.sh create_cluster`.
- SSH into any of the servers and load data by `./scripts/start.sh loaddata`.
- Build Java package on all servers by `./scripts/experiment.sh build`.
- Schedule experiments by `./scripts/experiment.sh run <num_of_java_instances> <concurrency_level>`.
    - There are 2 options for `<concurrency_level>`, `ONE_THREE` and `MAJORITY`.
    
## Analysis

- First, download the error log files from all servers by:

```bash
scp -r cs4224f@xcnd25:/temp/cs4224f/Wholesale-MongoDB/log/*.err.log <desired_local_folder_path>
scp -r cs4224f@xcnd26:/temp/cs4224f/Wholesale-MongoDB/log/*.err.log <desired_local_folder_path>
scp -r cs4224f@xcnd27:/temp/cs4224f/Wholesale-MongoDB/log/*.err.log <desired_local_folder_path>
scp -r cs4224f@xcnd28:/temp/cs4224f/Wholesale-MongoDB/log/*.err.log <desired_local_folder_path>
scp -r cs4224f@xcnd29:/temp/cs4224f/Wholesale-MongoDB/log/*.err.log <desired_local_folder_path>
```

- Then, strip the downloaded files to keep the performance report only by `./scripts/start.sh strip <desired_local_folder_path>`.
- Lastly, get overall statistics by `./scripts/start.sh stats <desired_local_folder_path> NC`.

## Licence

[GNU General Public Licence 3.0](LICENSE)
