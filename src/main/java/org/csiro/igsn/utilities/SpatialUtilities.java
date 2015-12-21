package org.csiro.igsn.utilities;


import org.csiro.igsn.bindings.allocation2_0.SpatialType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtilities {
	
	public static Geometry wktToGeometry(String wkt) {
		
		//VT: spatial store in lon/lat however mapping framework are often in lat/lon
				//http://postgis.net/2013/08/18/tip_lon_lat/						
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
        Geometry geom = null;
        try {
            geom = fromText.read(wkt);                        
        } catch (Exception e) {
            return null;
        }
        return geom;
    }
	
	public static Geometry wktToGeometry(String lat, String lon, SpatialType spatialType) {

		//VT: spatial store in lon/lat however mapping framework are often in lat/lon
		//http://postgis.net/2013/08/18/tip_lon_lat/
		String wkt="";
		if(spatialType == SpatialType.POINT){
			 wkt = String.format("Point(%s %s)", lon,lat);
		}
				
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
        Geometry geom = null;
        try {
            geom = fromText.read(wkt);                        
        } catch (Exception e) {
            return null;
        }
        return geom;
    }
	

	
	
//	public static Geometry wktToGeometry(CoordinateArrays latlon, SpatialType spatialType) {
//
//		//VT: spatial store in lon/lat however mapping framework are often in lat/lon
//		//http://postgis.net/2013/08/18/tip_lon_lat/
//		String wkt="";
//		if(spatialType == SpatialType.POINT){
//			wkt = String.format("Polygon(%s %s)", "","");
//		}
//				
//        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
//        Geometry geom = null;
//        try {
//            geom = fromText.read(wkt);                        
//        } catch (Exception e) {
//            return null;
//        }
//        return geom;
//    }
	
	

}
