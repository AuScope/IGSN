--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.3
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-08-05 15:29:35

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 7 (class 2615 OID 228924)
-- Name: topology; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA topology;


--
-- TOC entry 238 (class 3079 OID 11861)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 238
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 239 (class 3079 OID 227637)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 239
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- TOC entry 240 (class 3079 OID 228925)
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 240
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


SET search_path = public, pg_catalog;

--
-- TOC entry 193 (class 1259 OID 229109)
-- Name: allocator_allocator_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE allocator_allocator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_with_oids = false;

--
-- TOC entry 235 (class 1259 OID 229471)
-- Name: allocator; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE allocator (
    allocator_id integer DEFAULT nextval('allocator_allocator_id_seq'::regclass) NOT NULL,
    comments text,
    contact_email character varying(255) NOT NULL,
    contact_name character varying(100) NOT NULL,
    created timestamp without time zone,
    is_active boolean,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    allocator_prefix integer NOT NULL
);


--
-- TOC entry 195 (class 1259 OID 229123)
-- Name: cv_classification_classification_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_classification_classification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 194 (class 1259 OID 229118)
-- Name: cv_classification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_classification (
    classification_id integer DEFAULT nextval('cv_classification_classification_id_seq'::regclass) NOT NULL,
    identifier character varying(100) NOT NULL,
    terms character varying(300)
);


--
-- TOC entry 197 (class 1259 OID 229131)
-- Name: cv_resourcestypes_resource_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_resourcestypes_resource_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 196 (class 1259 OID 229126)
-- Name: cv_resourcestype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_resourcestype (
    resource_type_id integer DEFAULT nextval('cv_resourcestypes_resource_type_id_seq'::regclass) NOT NULL,
    name character varying(50) NOT NULL,
    link character varying(200)
);


--
-- TOC entry 199 (class 1259 OID 229143)
-- Name: cv_sampletype_sample_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_sampletype_sample_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 198 (class 1259 OID 229135)
-- Name: cv_sampletype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_sampletype (
    sample_type_id integer DEFAULT nextval('cv_sampletype_sample_type_id_seq'::regclass) NOT NULL,
    term character varying(255) NOT NULL,
    definition text,
    link text
);


--
-- TOC entry 201 (class 1259 OID 229154)
-- Name: cv_samplingfeature_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_samplingfeature_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 200 (class 1259 OID 229146)
-- Name: cv_samplingfeature; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_samplingfeature (
    feature_id integer DEFAULT nextval('cv_samplingfeature_feature_id_seq'::regclass) NOT NULL,
    name character varying(80),
    definition text,
    identifier character varying(255)
);


--
-- TOC entry 203 (class 1259 OID 229165)
-- Name: cv_samplingmethod_method_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_samplingmethod_method_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 202 (class 1259 OID 229157)
-- Name: cv_samplingmethod; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_samplingmethod (
    method_id integer DEFAULT nextval('cv_samplingmethod_method_id_seq'::regclass) NOT NULL,
    method_name character varying(80) NOT NULL,
    method_description text NOT NULL,
    method_link text
);


--
-- TOC entry 205 (class 1259 OID 229176)
-- Name: cv_spatialreferences_spatial_ref_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_spatialreferences_spatial_ref_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 204 (class 1259 OID 229168)
-- Name: cv_spatialreferences; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_spatialreferences (
    spatial_ref_id integer DEFAULT nextval('cv_spatialreferences_spatial_ref_id_seq'::regclass) NOT NULL,
    srs_code integer NOT NULL,
    srs_name character varying(255) NOT NULL,
    is_geographic boolean,
    description text
);


--
-- TOC entry 207 (class 1259 OID 229187)
-- Name: cv_units_units_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_units_units_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 206 (class 1259 OID 229179)
-- Name: cv_units; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_units (
    units_id integer DEFAULT nextval('cv_units_units_id_seq'::regclass) NOT NULL,
    units_name character varying(255) NOT NULL,
    units_type character varying(255) NOT NULL,
    units_abbreviation character varying(255) NOT NULL
);


--
-- TOC entry 208 (class 1259 OID 229190)
-- Name: cv_variables; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_variables (
    term character varying(255) NOT NULL,
    definition text
);


--
-- TOC entry 210 (class 1259 OID 229206)
-- Name: cv_verticaldatum_vertical_datum_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_verticaldatum_vertical_datum_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 209 (class 1259 OID 229198)
-- Name: cv_verticaldatum; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_verticaldatum (
    vertical_datum_id integer DEFAULT nextval('cv_verticaldatum_vertical_datum_id_seq'::regclass) NOT NULL,
    term character varying(255) NOT NULL,
    definition text
);


--
-- TOC entry 237 (class 1259 OID 229507)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 192 (class 1259 OID 229090)
-- Name: prefix_object_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE prefix_object_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 236 (class 1259 OID 229490)
-- Name: prefix; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE prefix (
    object_id integer DEFAULT nextval('prefix_object_id_seq'::regclass) NOT NULL,
    prefix character varying(50) NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    version integer NOT NULL
);


--
-- TOC entry 212 (class 1259 OID 229229)
-- Name: registrant_registrant_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE registrant_registrant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 211 (class 1259 OID 229209)
-- Name: registrant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE registrant (
    registrant_id integer DEFAULT nextval('registrant_registrant_id_seq'::regclass) NOT NULL,
    email character varying(255) NOT NULL,
    username character varying(80) NOT NULL,
    created timestamp without time zone DEFAULT now(),
    igsn_quota_allowed integer NOT NULL,
    igsn_quota_used integer NOT NULL,
    is_active boolean,
    name character varying(255) NOT NULL,
    password character varying NOT NULL,
    updated timestamp without time zone DEFAULT now() NOT NULL,
    allocator integer NOT NULL,
    registrant_prefix integer NOT NULL
);


--
-- TOC entry 214 (class 1259 OID 229287)
-- Name: sample_sample_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sample_sample_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 213 (class 1259 OID 229232)
-- Name: sample; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample (
    sample_id integer DEFAULT nextval('sample_sample_id_seq'::regclass) NOT NULL,
    sample_type integer NOT NULL,
    sample_name character varying(255) NOT NULL,
    other_name character varying(255),
    igsn character varying(255),
    classification integer,
    purpose text,
    is_public boolean NOT NULL,
    comment text,
    sampling_start timestamp without time zone,
    sampling_end timestamp without time zone,
    lat_long_datum_id integer,
    elevation double precision,
    elevation_unit integer,
    local_projection_id integer,
    locality character varying(200),
    sampling_method integer,
    sampling_feature_id integer,
    sample_size double precision,
    sample_size_unit integer,
    registrant integer NOT NULL,
    sampling_campaign text,
    created timestamp without time zone DEFAULT now(),
    modified timestamp without time zone DEFAULT now(),
    modified_by character varying(80),
    lat_lon geometry(Point,4326),
    localxy geometry(Point,4326),
    latlon_end geometry(Point,4326)
);


--
-- TOC entry 218 (class 1259 OID 229324)
-- Name: sample_features_mapping_object_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sample_features_mapping_object_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 217 (class 1259 OID 229309)
-- Name: sample_features_mapping; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample_features_mapping (
    object_id integer DEFAULT nextval('sample_features_mapping_object_id_seq'::regclass) NOT NULL,
    sample_id integer NOT NULL,
    feature_id integer NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 229340)
-- Name: samplecollector_collector_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplecollector_collector_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 219 (class 1259 OID 229327)
-- Name: samplecollector; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplecollector (
    collector_id integer DEFAULT nextval('samplecollector_collector_id_seq'::regclass) NOT NULL,
    collector text NOT NULL,
    sample_id integer NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 229356)
-- Name: samplecuration_sample_curation_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplecuration_sample_curation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 221 (class 1259 OID 229343)
-- Name: samplecuration; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplecuration (
    sample_curation_id integer DEFAULT nextval('samplecuration_sample_curation_id_seq'::regclass) NOT NULL,
    sample_id integer NOT NULL,
    curation_location text,
    curator text,
    curation_start timestamp without time zone,
    curation_end timestamp without time zone,
    comments text
);


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN samplecuration.curator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN samplecuration.curator IS '
';


--
-- TOC entry 226 (class 1259 OID 229385)
-- Name: samplegroup_sample_group_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplegroup_sample_group_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 225 (class 1259 OID 229370)
-- Name: samplegroup; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplegroup (
    group_id integer DEFAULT nextval('samplegroup_sample_group_id'::regclass) NOT NULL,
    sample_id integer NOT NULL,
    sample_group_id integer NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 229367)
-- Name: samplegroupdesc_group_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplegroupdesc_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 223 (class 1259 OID 229359)
-- Name: samplegroupdesc; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplegroupdesc (
    group_id integer DEFAULT nextval('samplegroupdesc_group_id_seq'::regclass) NOT NULL,
    group_description text
);


--
-- TOC entry 228 (class 1259 OID 229406)
-- Name: sampleresources_sample_resource_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sampleresources_sample_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 227 (class 1259 OID 229388)
-- Name: sampleresources; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sampleresources (
    sample_resource_id integer DEFAULT nextval('sampleresources_sample_resource_id_seq'::regclass) NOT NULL,
    sample_id integer NOT NULL,
    resource_type integer NOT NULL,
    resource_name character varying(150),
    resource_path character varying(500),
    added_by character varying(200),
    added_date timestamp without time zone
);


--
-- TOC entry 216 (class 1259 OID 229306)
-- Name: samplingfeatures_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplingfeatures_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 215 (class 1259 OID 229291)
-- Name: samplingfeatures; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplingfeatures (
    feature_id integer DEFAULT nextval('samplingfeatures_feature_id_seq'::regclass) NOT NULL,
    feature_name character varying(100) NOT NULL,
    feature_srs integer,
    feature_locality character varying(150),
    feature_type integer,
    feature_latlon geometry(Point,4326),
    feature_latlon_end geometry(Point,4326),
    feature_local_xy geometry(Point,4326)
);


--
-- TOC entry 230 (class 1259 OID 229417)
-- Name: sites_site_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sites_site_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 229 (class 1259 OID 229409)
-- Name: sites; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sites (
    site_id integer DEFAULT nextval('sites_site_id_seq'::regclass) NOT NULL,
    site_code character varying(80) NOT NULL,
    site_name character varying(255) NOT NULL,
    lat double precision NOT NULL,
    lon double precision NOT NULL,
    latlon_datum_id integer NOT NULL,
    site_type integer,
    elevation double precision,
    elevation_unit integer,
    vertical_datum integer,
    local_x double precision,
    local_y double precision,
    local_projection_id integer,
    state character varying(255),
    county character varying(255),
    comments text,
    bounding_box geometry(Geometry,4326)
);


--
-- TOC entry 232 (class 1259 OID 229428)
-- Name: users_source_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 231 (class 1259 OID 229420)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE users (
    source_id integer DEFAULT nextval('users_source_id_seq'::regclass) NOT NULL,
    organization character varying(255) NOT NULL,
    source_description text,
    source_link text,
    contact_name character varying(255) NOT NULL,
    phone character varying(255),
    email character varying(255),
    address character varying(255),
    city character varying(255),
    state character varying(255),
    zip_code character varying(255),
    metadata_id integer
);


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN users.source_link; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.source_link IS '

';


--
-- TOC entry 234 (class 1259 OID 229445)
-- Name: variables_variable_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE variables_variable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 233 (class 1259 OID 229432)
-- Name: variables; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE variables (
    variable_id integer DEFAULT nextval('variables_variable_id_seq'::regclass) NOT NULL,
    variable_code character varying(50) NOT NULL,
    variable_name character varying(255) NOT NULL,
    variable_units_id integer NOT NULL,
    sample_medium character varying(255) NOT NULL,
    value_type character varying(255) NOT NULL,
    data_type character varying(255) NOT NULL,
    general_category character varying(255) NOT NULL
);


--
-- TOC entry 3471 (class 2606 OID 229478)
-- Name: pk_allocator_allocator_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator
    ADD CONSTRAINT pk_allocator_allocator_id PRIMARY KEY (allocator_id);


--
-- TOC entry 3429 (class 2606 OID 229122)
-- Name: pk_cv_classification; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_classification
    ADD CONSTRAINT pk_cv_classification PRIMARY KEY (classification_id);


--
-- TOC entry 3431 (class 2606 OID 229130)
-- Name: pk_cv_resourcestypes_resource_type_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_resourcestype
    ADD CONSTRAINT pk_cv_resourcestypes_resource_type_id PRIMARY KEY (resource_type_id);


--
-- TOC entry 3433 (class 2606 OID 229142)
-- Name: pk_cv_sampletype_sample_type_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_sampletype
    ADD CONSTRAINT pk_cv_sampletype_sample_type_id PRIMARY KEY (sample_type_id);


--
-- TOC entry 3435 (class 2606 OID 229153)
-- Name: pk_cv_samplingfeature_feature_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingfeature
    ADD CONSTRAINT pk_cv_samplingfeature_feature_id PRIMARY KEY (feature_id);


--
-- TOC entry 3437 (class 2606 OID 229164)
-- Name: pk_cv_samplingmethod_method_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingmethod
    ADD CONSTRAINT pk_cv_samplingmethod_method_id PRIMARY KEY (method_id);


--
-- TOC entry 3439 (class 2606 OID 229175)
-- Name: pk_cv_spatialreferences_spatial_ref_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_spatialreferences
    ADD CONSTRAINT pk_cv_spatialreferences_spatial_ref_id PRIMARY KEY (spatial_ref_id);


--
-- TOC entry 3441 (class 2606 OID 229186)
-- Name: pk_cv_units_units_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_units
    ADD CONSTRAINT pk_cv_units_units_id PRIMARY KEY (units_id);


--
-- TOC entry 3443 (class 2606 OID 229197)
-- Name: pk_cv_variables_term; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_variables
    ADD CONSTRAINT pk_cv_variables_term PRIMARY KEY (term);


--
-- TOC entry 3445 (class 2606 OID 229205)
-- Name: pk_cv_verticaldatum_vertical_datum_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_verticaldatum
    ADD CONSTRAINT pk_cv_verticaldatum_vertical_datum_id PRIMARY KEY (vertical_datum_id);


--
-- TOC entry 3473 (class 2606 OID 229496)
-- Name: pk_prefix_object_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY prefix
    ADD CONSTRAINT pk_prefix_object_id PRIMARY KEY (object_id);


--
-- TOC entry 3447 (class 2606 OID 229218)
-- Name: pk_registrant_registrant_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT pk_registrant_registrant_id PRIMARY KEY (registrant_id);


--
-- TOC entry 3453 (class 2606 OID 229313)
-- Name: pk_sample_features_mapping_object_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT pk_sample_features_mapping_object_id PRIMARY KEY (object_id);


--
-- TOC entry 3449 (class 2606 OID 229241)
-- Name: pk_sample_sample_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT pk_sample_sample_id PRIMARY KEY (sample_id);


--
-- TOC entry 3455 (class 2606 OID 229334)
-- Name: pk_samplecollector_collector_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecollector
    ADD CONSTRAINT pk_samplecollector_collector_id PRIMARY KEY (collector_id);


--
-- TOC entry 3457 (class 2606 OID 229350)
-- Name: pk_samplecuration_sample_curation_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecuration
    ADD CONSTRAINT pk_samplecuration_sample_curation_id PRIMARY KEY (sample_curation_id);


--
-- TOC entry 3461 (class 2606 OID 229374)
-- Name: pk_samplegroup_sample_group_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplegroup
    ADD CONSTRAINT pk_samplegroup_sample_group_id PRIMARY KEY (sample_group_id);


--
-- TOC entry 3459 (class 2606 OID 229366)
-- Name: pk_samplegroupdesc_group_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplegroupdesc
    ADD CONSTRAINT pk_samplegroupdesc_group_id PRIMARY KEY (group_id);


--
-- TOC entry 3463 (class 2606 OID 229395)
-- Name: pk_sampleresources_sample_resource_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT pk_sampleresources_sample_resource_id PRIMARY KEY (sample_resource_id);


--
-- TOC entry 3451 (class 2606 OID 229295)
-- Name: pk_samplingfeatures_feature_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures
    ADD CONSTRAINT pk_samplingfeatures_feature_id PRIMARY KEY (feature_id);


--
-- TOC entry 3465 (class 2606 OID 229416)
-- Name: pk_sites_site_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sites
    ADD CONSTRAINT pk_sites_site_id PRIMARY KEY (site_id);


--
-- TOC entry 3467 (class 2606 OID 229427)
-- Name: pk_users_source_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_users_source_id PRIMARY KEY (source_id);


--
-- TOC entry 3469 (class 2606 OID 229439)
-- Name: pk_variables_variable_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY variables
    ADD CONSTRAINT pk_variables_variable_id PRIMARY KEY (variable_id);


--
-- TOC entry 3496 (class 2606 OID 229497)
-- Name: fk_allocator_allocator_prefix; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator
    ADD CONSTRAINT fk_allocator_allocator_prefix FOREIGN KEY (allocator_prefix) REFERENCES prefix(object_id);


--
-- TOC entry 3474 (class 2606 OID 229484)
-- Name: fk_registrant_allocator; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT fk_registrant_allocator FOREIGN KEY (allocator) REFERENCES allocator(allocator_id);


--
-- TOC entry 3475 (class 2606 OID 229502)
-- Name: fk_registrant_registrant_prefix; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT fk_registrant_registrant_prefix FOREIGN KEY (registrant_prefix) REFERENCES prefix(object_id);


--
-- TOC entry 3476 (class 2606 OID 229242)
-- Name: fk_sample_classification; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_classification FOREIGN KEY (classification) REFERENCES cv_classification(classification_id);


--
-- TOC entry 3477 (class 2606 OID 229247)
-- Name: fk_sample_elevation_unit; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_elevation_unit FOREIGN KEY (elevation_unit) REFERENCES cv_units(units_id);


--
-- TOC entry 3478 (class 2606 OID 229252)
-- Name: fk_sample_feature; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_feature FOREIGN KEY (sampling_feature_id) REFERENCES cv_samplingfeature(feature_id);


--
-- TOC entry 3487 (class 2606 OID 229314)
-- Name: fk_sample_features_mapping_feature_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT fk_sample_features_mapping_feature_id FOREIGN KEY (feature_id) REFERENCES cv_samplingfeature(feature_id);


--
-- TOC entry 3488 (class 2606 OID 229319)
-- Name: fk_sample_features_mapping_sample_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT fk_sample_features_mapping_sample_id FOREIGN KEY (sample_id) REFERENCES sample(sample_id);


--
-- TOC entry 3483 (class 2606 OID 229277)
-- Name: fk_sample_lat_long_datum; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_lat_long_datum FOREIGN KEY (lat_long_datum_id) REFERENCES cv_spatialreferences(spatial_ref_id);


--
-- TOC entry 3484 (class 2606 OID 229282)
-- Name: fk_sample_local_projection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_local_projection_id FOREIGN KEY (local_projection_id) REFERENCES cv_spatialreferences(spatial_ref_id);


--
-- TOC entry 3479 (class 2606 OID 229257)
-- Name: fk_sample_registrant; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_registrant FOREIGN KEY (registrant) REFERENCES registrant(registrant_id);


--
-- TOC entry 3482 (class 2606 OID 229272)
-- Name: fk_sample_sample_size_unit; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_sample_size_unit FOREIGN KEY (sample_size_unit) REFERENCES cv_units(units_id);


--
-- TOC entry 3480 (class 2606 OID 229262)
-- Name: fk_sample_sample_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_sample_type FOREIGN KEY (sample_type) REFERENCES cv_sampletype(sample_type_id);


--
-- TOC entry 3481 (class 2606 OID 229267)
-- Name: fk_sample_sampling_method; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_sampling_method FOREIGN KEY (sampling_method) REFERENCES cv_samplingmethod(method_id);


--
-- TOC entry 3489 (class 2606 OID 229335)
-- Name: fk_samplecollector_sample_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecollector
    ADD CONSTRAINT fk_samplecollector_sample_id FOREIGN KEY (sample_id) REFERENCES sample(sample_id);


--
-- TOC entry 3490 (class 2606 OID 229351)
-- Name: fk_samplecuration_sample_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecuration
    ADD CONSTRAINT fk_samplecuration_sample_id FOREIGN KEY (sample_id) REFERENCES sample(sample_id);


--
-- TOC entry 3492 (class 2606 OID 229380)
-- Name: fk_samplegroup_group_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplegroup
    ADD CONSTRAINT fk_samplegroup_group_id FOREIGN KEY (group_id) REFERENCES samplegroupdesc(group_id);


--
-- TOC entry 3491 (class 2606 OID 229375)
-- Name: fk_samplegroup_sample_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplegroup
    ADD CONSTRAINT fk_samplegroup_sample_id FOREIGN KEY (sample_id) REFERENCES sample(sample_id);


--
-- TOC entry 3494 (class 2606 OID 229401)
-- Name: fk_sampleresources_resource_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT fk_sampleresources_resource_type FOREIGN KEY (resource_type) REFERENCES cv_resourcestype(resource_type_id);


--
-- TOC entry 3493 (class 2606 OID 229396)
-- Name: fk_sampleresources_sample_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT fk_sampleresources_sample_id FOREIGN KEY (sample_id) REFERENCES sample(sample_id);


--
-- TOC entry 3486 (class 2606 OID 229296)
-- Name: fk_samplingfeatures_feature_srs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures
    ADD CONSTRAINT fk_samplingfeatures_feature_srs FOREIGN KEY (feature_srs) REFERENCES cv_spatialreferences(spatial_ref_id);


--
-- TOC entry 3485 (class 2606 OID 229301)
-- Name: fk_samplingfeatures_feature_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures
    ADD CONSTRAINT fk_samplingfeatures_feature_type FOREIGN KEY (feature_type) REFERENCES cv_samplingfeature(feature_id);


--
-- TOC entry 3495 (class 2606 OID 229440)
-- Name: fk_variables_variable_units; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY variables
    ADD CONSTRAINT fk_variables_variable_units FOREIGN KEY (variable_units_id) REFERENCES cv_units(units_id);


-- Completed on 2015-08-05 15:29:36

--
-- PostgreSQL database dump complete
--

