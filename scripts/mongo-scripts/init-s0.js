rs.initiate(
    {
        _id: "s0",
        configsvr: true,
        members: [
            { _id : 0, host : "xcnd25.comp.nus.edu.sg:28000" },
            { _id : 1, host : "xcnd26.comp.nus.edu.sg:28000" },
            { _id : 2, host : "xcnd27.comp.nus.edu.sg:28000" }
        ]
    }
);
