package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * GeometryColumnsId generated by hbm2java
 */
@Embeddable
public class GeometryColumnsId implements java.io.Serializable {

	private String FTableCatalog;
	private String FTableSchema;
	private String FTableName;
	private String FGeometryColumn;
	private Integer coordDimension;
	private Integer srid;
	private String type;

	public GeometryColumnsId() {
	}

	public GeometryColumnsId(String FTableCatalog, String FTableSchema,
			String FTableName, String FGeometryColumn, Integer coordDimension,
			Integer srid, String type) {
		this.FTableCatalog = FTableCatalog;
		this.FTableSchema = FTableSchema;
		this.FTableName = FTableName;
		this.FGeometryColumn = FGeometryColumn;
		this.coordDimension = coordDimension;
		this.srid = srid;
		this.type = type;
	}

	@Column(name = "f_table_catalog", length = 256)
	public String getFTableCatalog() {
		return this.FTableCatalog;
	}

	public void setFTableCatalog(String FTableCatalog) {
		this.FTableCatalog = FTableCatalog;
	}

	@Column(name = "f_table_schema", length = 256)
	public String getFTableSchema() {
		return this.FTableSchema;
	}

	public void setFTableSchema(String FTableSchema) {
		this.FTableSchema = FTableSchema;
	}

	@Column(name = "f_table_name", length = 256)
	public String getFTableName() {
		return this.FTableName;
	}

	public void setFTableName(String FTableName) {
		this.FTableName = FTableName;
	}

	@Column(name = "f_geometry_column", length = 256)
	public String getFGeometryColumn() {
		return this.FGeometryColumn;
	}

	public void setFGeometryColumn(String FGeometryColumn) {
		this.FGeometryColumn = FGeometryColumn;
	}

	@Column(name = "coord_dimension")
	public Integer getCoordDimension() {
		return this.coordDimension;
	}

	public void setCoordDimension(Integer coordDimension) {
		this.coordDimension = coordDimension;
	}

	@Column(name = "srid")
	public Integer getSrid() {
		return this.srid;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	@Column(name = "type", length = 30)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GeometryColumnsId))
			return false;
		GeometryColumnsId castOther = (GeometryColumnsId) other;

		return ((this.getFTableCatalog() == castOther.getFTableCatalog()) || (this
				.getFTableCatalog() != null
				&& castOther.getFTableCatalog() != null && this
				.getFTableCatalog().equals(castOther.getFTableCatalog())))
				&& ((this.getFTableSchema() == castOther.getFTableSchema()) || (this
						.getFTableSchema() != null
						&& castOther.getFTableSchema() != null && this
						.getFTableSchema().equals(castOther.getFTableSchema())))
				&& ((this.getFTableName() == castOther.getFTableName()) || (this
						.getFTableName() != null
						&& castOther.getFTableName() != null && this
						.getFTableName().equals(castOther.getFTableName())))
				&& ((this.getFGeometryColumn() == castOther
						.getFGeometryColumn()) || (this.getFGeometryColumn() != null
						&& castOther.getFGeometryColumn() != null && this
						.getFGeometryColumn().equals(
								castOther.getFGeometryColumn())))
				&& ((this.getCoordDimension() == castOther.getCoordDimension()) || (this
						.getCoordDimension() != null
						&& castOther.getCoordDimension() != null && this
						.getCoordDimension().equals(
								castOther.getCoordDimension())))
				&& ((this.getSrid() == castOther.getSrid()) || (this.getSrid() != null
						&& castOther.getSrid() != null && this.getSrid()
						.equals(castOther.getSrid())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null
						&& castOther.getType() != null && this.getType()
						.equals(castOther.getType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getFTableCatalog() == null ? 0 : this.getFTableCatalog()
						.hashCode());
		result = 37
				* result
				+ (getFTableSchema() == null ? 0 : this.getFTableSchema()
						.hashCode());
		result = 37
				* result
				+ (getFTableName() == null ? 0 : this.getFTableName()
						.hashCode());
		result = 37
				* result
				+ (getFGeometryColumn() == null ? 0 : this.getFGeometryColumn()
						.hashCode());
		result = 37
				* result
				+ (getCoordDimension() == null ? 0 : this.getCoordDimension()
						.hashCode());
		result = 37 * result
				+ (getSrid() == null ? 0 : this.getSrid().hashCode());
		result = 37 * result
				+ (getType() == null ? 0 : this.getType().hashCode());
		return result;
	}

}
