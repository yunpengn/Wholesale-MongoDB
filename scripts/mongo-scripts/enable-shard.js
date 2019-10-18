// Enables sharding on the database level.
sh.enableSharding("wholesale");

// Enables sharding on the per collection level.
sh.shardCollection("wholesale.warehouse", {
    w_id: 1
});

sh.shardCollection("wholesale.district", {
    d_w_id: 1,
    d_id: 1
});

sh.shardCollection("wholesale.customer", {
    c_w_id: 1,
    c_d_id: 1
});

sh.shardCollection("wholesale.order", {
    o_w_id: 1,
    o_d_id: 1
});

sh.shardCollection("wholesale.item", {
    i_id: 1
});

sh.shardCollection("wholesale.stock", {
    s_w_id: 1
});
