use wholesale;

// Enables sharding on the database level.
sh.enableSharding("wholesale");

// Enables sharding on the per collection level.
sh.shardCollection("wholesale.warehouse", {
    w_ID: 1
});

sh.shardCollection("wholesale.district", {
    d_W_ID: 1,
    d_ID: 1
});

sh.shardCollection("wholesale.customer", {
    c_W_ID: 1,
    c_D_ID: 1,
    c_ID: 1
});

sh.shardCollection("wholesale.order", {
    o_W_ID: 1,
    o_D_ID: 1,
    o_ID: 1
});

sh.shardCollection("wholesale.item", {
    i_ID: 1
});

sh.shardCollection("wholesale.stock", {
    s_W_ID: 1,
    s_I_ID: 1
});
