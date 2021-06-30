package com.reapex.sv.daodb;

import com.reapex.sv.entitydaodb.Region;

import java.util.List;

public class RegionDao {
    public List<Region> getRegionList() {
        List<Region> regionList = Region.findWithQuery(Region.class,
                "select * from region order by seq asc");
        return regionList;
    }
}
