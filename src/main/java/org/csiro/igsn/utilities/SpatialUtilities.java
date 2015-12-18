package org.csiro.igsn.utilities;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtilities {
	
	public static Geometry wktToGeometry(String lat, String lon) {

		//VT: spatial store in lon/lat however mapping framework are often in lat/lon
		//http://postgis.net/2013/08/18/tip_lon_lat/
		String wkt = String.format("Point(%s %s)", lon,lat);
				
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
        Geometry geom = null;
        try {
            geom = fromText.read(wkt);                        
        } catch (Exception e) {
            return null;
        }
        return geom;
    }
	
	public static Geometry wktToGeometry(Double lat, Double lon,String datum)  {
		return SpatialUtilities.wktToGeometry(String.valueOf(lat),String.valueOf(lon));
		
	}

}
