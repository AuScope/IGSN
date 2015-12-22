--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-12-22 11:14:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 241 (class 1259 OID 33992)
-- Name: allocator; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE allocator (
    allocatorid integer NOT NULL,
    comments text,
    contactemail character varying(255) NOT NULL,
    contactname character varying(80) NOT NULL,
    created timestamp without time zone,
    username character varying(50) NOT NULL,
    password character varying(50),
    isactive boolean
);


--
-- TOC entry 242 (class 1259 OID 33998)
-- Name: allocator_allocatorid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE allocator_allocatorid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3825 (class 0 OID 0)
-- Dependencies: 242
-- Name: allocator_allocatorid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE allocator_allocatorid_seq OWNED BY allocator.allocatorid;


--
-- TOC entry 243 (class 1259 OID 34000)
-- Name: allocator_prefixes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE allocator_prefixes (
    allocator integer NOT NULL,
    prefixes integer NOT NULL
);


--
-- TOC entry 244 (class 1259 OID 34003)
-- Name: cv_related_identifiertype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_related_identifiertype (
    objectid integer NOT NULL,
    relatedidentifiertype character varying(20) NOT NULL
);


--
-- TOC entry 245 (class 1259 OID 34006)
-- Name: cv_related_identifiertype_objectid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_related_identifiertype_objectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3826 (class 0 OID 0)
-- Dependencies: 245
-- Name: cv_related_identifiertype_objectid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_related_identifiertype_objectid_seq OWNED BY cv_related_identifiertype.objectid;


--
-- TOC entry 246 (class 1259 OID 34008)
-- Name: cv_resource_relationshiptype_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_resource_relationshiptype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 247 (class 1259 OID 34010)
-- Name: cv_resource_relationshiptype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_resource_relationshiptype (
    id integer DEFAULT nextval('cv_resource_relationshiptype_seq'::regclass) NOT NULL,
    relationship_type character varying(100)
);


--
-- TOC entry 248 (class 1259 OID 34014)
-- Name: cv_samplematerial; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_samplematerial (
    materialid integer NOT NULL,
    materialidentifier character varying(250) NOT NULL,
    materialdesc character varying(45)
);


--
-- TOC entry 249 (class 1259 OID 34017)
-- Name: cv_samplematerial_materialid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_samplematerial_materialid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3827 (class 0 OID 0)
-- Dependencies: 249
-- Name: cv_samplematerial_materialid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_samplematerial_materialid_seq OWNED BY cv_samplematerial.materialid;


--
-- TOC entry 250 (class 1259 OID 34019)
-- Name: cv_sampletype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_sampletype (
    sampletypeid integer NOT NULL,
    sampletypeidentifier character varying(255) NOT NULL,
    sampletypedefinition text
);


--
-- TOC entry 251 (class 1259 OID 34025)
-- Name: cv_sampletype_sampletypeid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_sampletype_sampletypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3828 (class 0 OID 0)
-- Dependencies: 251
-- Name: cv_sampletype_sampletypeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_sampletype_sampletypeid_seq OWNED BY cv_sampletype.sampletypeid;


--
-- TOC entry 252 (class 1259 OID 34027)
-- Name: cv_samplingfeature; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_samplingfeature (
    featureid integer NOT NULL,
    name character varying(80),
    definition text,
    identifier character varying(255) NOT NULL
);


--
-- TOC entry 253 (class 1259 OID 34033)
-- Name: cv_samplingfeature_featureid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_samplingfeature_featureid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3829 (class 0 OID 0)
-- Dependencies: 253
-- Name: cv_samplingfeature_featureid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_samplingfeature_featureid_seq OWNED BY cv_samplingfeature.featureid;


--
-- TOC entry 254 (class 1259 OID 34035)
-- Name: cv_samplingmethod; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv_samplingmethod (
    methodid integer NOT NULL,
    methodidentifier character varying(255) NOT NULL,
    methoddescription text NOT NULL
);


--
-- TOC entry 255 (class 1259 OID 34041)
-- Name: cv_samplingmethod_methodid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_samplingmethod_methodid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3830 (class 0 OID 0)
-- Dependencies: 255
-- Name: cv_samplingmethod_methodid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_samplingmethod_methodid_seq OWNED BY cv_samplingmethod.methodid;


--
-- TOC entry 256 (class 1259 OID 34043)
-- Name: prefix; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE prefix (
    id integer NOT NULL,
    prefix character varying(15) NOT NULL,
    created timestamp without time zone,
    version integer
);


--
-- TOC entry 257 (class 1259 OID 34046)
-- Name: prefix_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE prefix_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3831 (class 0 OID 0)
-- Dependencies: 257
-- Name: prefix_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE prefix_id_seq OWNED BY prefix.id;


--
-- TOC entry 258 (class 1259 OID 34048)
-- Name: registrant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE registrant (
    registrantid integer NOT NULL,
    registrantname character varying(255) NOT NULL,
    registrantemail character varying(255) NOT NULL,
    created timestamp without time zone,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    updated timestamp without time zone,
    allocator integer NOT NULL,
    isactive boolean
);


--
-- TOC entry 259 (class 1259 OID 34054)
-- Name: registrant_prefixes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE registrant_prefixes (
    registrant integer NOT NULL,
    prefixes integer NOT NULL
);


--
-- TOC entry 260 (class 1259 OID 34057)
-- Name: registrant_registrantid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE registrant_registrantid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3832 (class 0 OID 0)
-- Dependencies: 260
-- Name: registrant_registrantid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE registrant_registrantid_seq OWNED BY registrant.registrantid;


--
-- TOC entry 261 (class 1259 OID 34059)
-- Name: sample; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample (
    sampleid integer NOT NULL,
    samplename character varying(255) NOT NULL,
    othername character varying(255),
    igsn character varying(255) NOT NULL,
    landingpage character varying(250) NOT NULL,
    classification character varying(255),
    classificationidentifier character varying(255),
    purpose text,
    samplinglocgeom geometry(Geometry,4326),
    samplinglocsrs character varying(20),
    elevation character varying(30),
    verticaldatum character varying(20),
    locality character varying(300),
    samplingstart timestamp without time zone,
    samplingend timestamp without time zone,
    samplingmethod integer,
    samplingcampaign text,
    comment text,
    registrant integer NOT NULL,
    created timestamp without time zone NOT NULL,
    modified timestamp without time zone,
    physicalsamplestatus integer,
    registrationstatus integer,
    ispublic boolean,
    elevation_units character varying(30),
    samplingloc_nilreason character varying(100),
    samplingtime_nilreason character varying(100)
);


--
-- TOC entry 262 (class 1259 OID 34065)
-- Name: sample_collector; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample_collector (
    collectorid integer NOT NULL,
    collector text NOT NULL,
    collectoridentifier character varying(255),
    sampleid integer NOT NULL
);


--
-- TOC entry 263 (class 1259 OID 34071)
-- Name: sample_collector_collectorid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sample_collector_collectorid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3833 (class 0 OID 0)
-- Dependencies: 263
-- Name: sample_collector_collectorid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sample_collector_collectorid_seq OWNED BY sample_collector.collectorid;


--
-- TOC entry 264 (class 1259 OID 34073)
-- Name: sample_features_mapping; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample_features_mapping (
    objectid integer NOT NULL,
    sampleid integer NOT NULL,
    featureid integer NOT NULL
);


--
-- TOC entry 265 (class 1259 OID 34076)
-- Name: sample_features_mapping_objectid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sample_features_mapping_objectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3834 (class 0 OID 0)
-- Dependencies: 265
-- Name: sample_features_mapping_objectid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sample_features_mapping_objectid_seq OWNED BY sample_features_mapping.objectid;


--
-- TOC entry 266 (class 1259 OID 34078)
-- Name: sample_material; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample_material (
    sampleid integer NOT NULL,
    materialid integer NOT NULL
);


--
-- TOC entry 267 (class 1259 OID 34081)
-- Name: sample_sampleid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sample_sampleid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3835 (class 0 OID 0)
-- Dependencies: 267
-- Name: sample_sampleid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sample_sampleid_seq OWNED BY sample.sampleid;


--
-- TOC entry 268 (class 1259 OID 34083)
-- Name: sample_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sample_types (
    sampleid integer NOT NULL,
    sampletypeid integer NOT NULL
);


--
-- TOC entry 269 (class 1259 OID 34086)
-- Name: samplecuration; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplecuration (
    samplecurationid integer NOT NULL,
    sampleid integer NOT NULL,
    curationlocation text,
    curator text NOT NULL,
    curationstart timestamp without time zone,
    curationend timestamp without time zone,
    comments text
);


--
-- TOC entry 270 (class 1259 OID 34092)
-- Name: samplecuration_samplecurationid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplecuration_samplecurationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3836 (class 0 OID 0)
-- Dependencies: 270
-- Name: samplecuration_samplecurationid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE samplecuration_samplecurationid_seq OWNED BY samplecuration.samplecurationid;


--
-- TOC entry 271 (class 1259 OID 34094)
-- Name: sampledfeatures_featureid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sampledfeatures_featureid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 272 (class 1259 OID 34096)
-- Name: sampledfeatures; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sampledfeatures (
    featureid integer DEFAULT nextval('sampledfeatures_featureid_seq'::regclass) NOT NULL,
    featuretype character varying(200) NOT NULL,
    featurename character varying(200) NOT NULL
);


--
-- TOC entry 273 (class 1259 OID 34100)
-- Name: sampleresources; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sampleresources (
    sampleresourcesid integer NOT NULL,
    sampleid integer NOT NULL,
    resourceidentifier character varying(250) NOT NULL,
    resourceidentifertype integer,
    addeddate timestamp without time zone,
    resourcerelationtype integer
);


--
-- TOC entry 274 (class 1259 OID 34103)
-- Name: sampleresources_sampleresourcesid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sampleresources_sampleresourcesid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3837 (class 0 OID 0)
-- Dependencies: 274
-- Name: sampleresources_sampleresourcesid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sampleresources_sampleresourcesid_seq OWNED BY sampleresources.sampleresourcesid;


--
-- TOC entry 275 (class 1259 OID 34105)
-- Name: sampling_sampled_mapping; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE sampling_sampled_mapping (
    samplingfeature_id integer,
    sampledfeature_id integer
);


--
-- TOC entry 276 (class 1259 OID 34108)
-- Name: samplingfeatures; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE samplingfeatures (
    featureid integer NOT NULL,
    featurename character varying(100) NOT NULL,
    featuregeom geometry(Geometry,4326),
    featuresrs character varying(20),
    elevation character varying(30),
    verticaldatum character varying(20),
    featurelocality character varying(150),
    featuretype integer,
    elevation_units character varying(30)
);


--
-- TOC entry 277 (class 1259 OID 34114)
-- Name: samplingfeatures_featureid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE samplingfeatures_featureid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3838 (class 0 OID 0)
-- Dependencies: 277
-- Name: samplingfeatures_featureid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE samplingfeatures_featureid_seq OWNED BY samplingfeatures.featureid;


--
-- TOC entry 278 (class 1259 OID 34116)
-- Name: status; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE status (
    statusid integer NOT NULL,
    statuscode character varying(25) NOT NULL,
    statusabout character varying(45),
    statusdesc character varying(100)
);


--
-- TOC entry 279 (class 1259 OID 34119)
-- Name: status_statusid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE status_statusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3839 (class 0 OID 0)
-- Dependencies: 279
-- Name: status_statusid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE status_statusid_seq OWNED BY status.statusid;


--
-- TOC entry 3553 (class 2604 OID 34121)
-- Name: allocatorid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator ALTER COLUMN allocatorid SET DEFAULT nextval('allocator_allocatorid_seq'::regclass);


--
-- TOC entry 3554 (class 2604 OID 34122)
-- Name: objectid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_related_identifiertype ALTER COLUMN objectid SET DEFAULT nextval('cv_related_identifiertype_objectid_seq'::regclass);


--
-- TOC entry 3556 (class 2604 OID 34123)
-- Name: materialid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplematerial ALTER COLUMN materialid SET DEFAULT nextval('cv_samplematerial_materialid_seq'::regclass);


--
-- TOC entry 3557 (class 2604 OID 34124)
-- Name: sampletypeid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_sampletype ALTER COLUMN sampletypeid SET DEFAULT nextval('cv_sampletype_sampletypeid_seq'::regclass);


--
-- TOC entry 3558 (class 2604 OID 34125)
-- Name: featureid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingfeature ALTER COLUMN featureid SET DEFAULT nextval('cv_samplingfeature_featureid_seq'::regclass);


--
-- TOC entry 3559 (class 2604 OID 34126)
-- Name: methodid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingmethod ALTER COLUMN methodid SET DEFAULT nextval('cv_samplingmethod_methodid_seq'::regclass);


--
-- TOC entry 3560 (class 2604 OID 34127)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY prefix ALTER COLUMN id SET DEFAULT nextval('prefix_id_seq'::regclass);


--
-- TOC entry 3561 (class 2604 OID 34128)
-- Name: registrantid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant ALTER COLUMN registrantid SET DEFAULT nextval('registrant_registrantid_seq'::regclass);


--
-- TOC entry 3562 (class 2604 OID 34129)
-- Name: sampleid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample ALTER COLUMN sampleid SET DEFAULT nextval('sample_sampleid_seq'::regclass);


--
-- TOC entry 3563 (class 2604 OID 34130)
-- Name: collectorid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_collector ALTER COLUMN collectorid SET DEFAULT nextval('sample_collector_collectorid_seq'::regclass);


--
-- TOC entry 3564 (class 2604 OID 34131)
-- Name: objectid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping ALTER COLUMN objectid SET DEFAULT nextval('sample_features_mapping_objectid_seq'::regclass);


--
-- TOC entry 3565 (class 2604 OID 34132)
-- Name: samplecurationid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecuration ALTER COLUMN samplecurationid SET DEFAULT nextval('samplecuration_samplecurationid_seq'::regclass);


--
-- TOC entry 3567 (class 2604 OID 34133)
-- Name: sampleresourcesid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources ALTER COLUMN sampleresourcesid SET DEFAULT nextval('sampleresources_sampleresourcesid_seq'::regclass);


--
-- TOC entry 3568 (class 2604 OID 34134)
-- Name: featureid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures ALTER COLUMN featureid SET DEFAULT nextval('samplingfeatures_featureid_seq'::regclass);


--
-- TOC entry 3569 (class 2604 OID 34135)
-- Name: statusid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY status ALTER COLUMN statusid SET DEFAULT nextval('status_statusid_seq'::regclass);


--
-- TOC entry 3781 (class 0 OID 33992)
-- Dependencies: 241
-- Data for Name: allocator; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO allocator VALUES (8, 'ADMIN', 'victor.tey@csiro.au', 'Victor', '2015-12-01 12:39:49.000992', 'tey006', NULL, true);
INSERT INTO allocator VALUES (7, 'ADMIN', 'anusuriya.devaraju@csiro.au', 'Anu', '2012-06-10 00:00:00', 'deva', 'suriya', NULL);


--
-- TOC entry 3840 (class 0 OID 0)
-- Dependencies: 242
-- Name: allocator_allocatorid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('allocator_allocatorid_seq', 8, true);


--
-- TOC entry 3783 (class 0 OID 34000)
-- Dependencies: 243
-- Data for Name: allocator_prefixes; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO allocator_prefixes VALUES (7, 14);
INSERT INTO allocator_prefixes VALUES (8, 14);


--
-- TOC entry 3784 (class 0 OID 34003)
-- Dependencies: 244
-- Data for Name: cv_related_identifiertype; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_related_identifiertype VALUES (1, 'doi');
INSERT INTO cv_related_identifiertype VALUES (2, 'handle');
INSERT INTO cv_related_identifiertype VALUES (4, 'url');
INSERT INTO cv_related_identifiertype VALUES (5, 'urn');
INSERT INTO cv_related_identifiertype VALUES (3, 'lsid');


--
-- TOC entry 3841 (class 0 OID 0)
-- Dependencies: 245
-- Name: cv_related_identifiertype_objectid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_related_identifiertype_objectid_seq', 5, true);


--
-- TOC entry 3787 (class 0 OID 34010)
-- Dependencies: 247
-- Data for Name: cv_resource_relationshiptype; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_resource_relationshiptype VALUES (1, 'IsCitedBy');
INSERT INTO cv_resource_relationshiptype VALUES (2, 'IsPartOf');
INSERT INTO cv_resource_relationshiptype VALUES (3, 'HasPart');
INSERT INTO cv_resource_relationshiptype VALUES (4, 'IsReferencedBy');
INSERT INTO cv_resource_relationshiptype VALUES (5, 'References');
INSERT INTO cv_resource_relationshiptype VALUES (6, 'IsDocumentedBy');
INSERT INTO cv_resource_relationshiptype VALUES (7, 'Documents');
INSERT INTO cv_resource_relationshiptype VALUES (8, 'IsCompiledBy');
INSERT INTO cv_resource_relationshiptype VALUES (9, 'Compiles');
INSERT INTO cv_resource_relationshiptype VALUES (10, 'IsVariantFormOf');
INSERT INTO cv_resource_relationshiptype VALUES (11, 'IsOriginalFormOf');


--
-- TOC entry 3842 (class 0 OID 0)
-- Dependencies: 246
-- Name: cv_resource_relationshiptype_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_resource_relationshiptype_seq', 11, true);


--
-- TOC entry 3788 (class 0 OID 34014)
-- Dependencies: 248
-- Data for Name: cv_samplematerial; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_samplematerial VALUES (1, 'http://vocabulary.odm2.org/medium/air', 'material desc');
INSERT INTO cv_samplematerial VALUES (2, 'http://vocabulary.odm2.org/medium/gas', 'material 2 desc');
INSERT INTO cv_samplematerial VALUES (3, 'http://vocabulary.odm2.org/medium/ice', 'ice');
INSERT INTO cv_samplematerial VALUES (4, 'http://vocabulary.odm2.org/medium/liquidAqueous', 'Liquid Aqueous');
INSERT INTO cv_samplematerial VALUES (5, 'http://vocabulary.odm2.org/medium/liquidOrganic', 'liquid organic');
INSERT INTO cv_samplematerial VALUES (6, 'http://vocabulary.odm2.org/medium/mineral', 'Mineral');
INSERT INTO cv_samplematerial VALUES (7, 'http://vocabulary.odm2.org/medium/notApplicable', 'Not Applicable');
INSERT INTO cv_samplematerial VALUES (8, 'http://vocabulary.odm2.org/medium/organism', 'organism');
INSERT INTO cv_samplematerial VALUES (9, 'http://vocabulary.odm2.org/medium/other', 'Other');
INSERT INTO cv_samplematerial VALUES (10, 'http://vocabulary.odm2.org/medium/particulate', 'Particulate');
INSERT INTO cv_samplematerial VALUES (11, 'http://vocabulary.odm2.org/medium/rock', 'Rock');
INSERT INTO cv_samplematerial VALUES (12, 'http://vocabulary.odm2.org/medium/sediment', 'Sediment');
INSERT INTO cv_samplematerial VALUES (13, 'http://vocabulary.odm2.org/medium/snow', 'Snow');
INSERT INTO cv_samplematerial VALUES (14, 'http://vocabulary.odm2.org/medium/soil', 'Soil');
INSERT INTO cv_samplematerial VALUES (15, 'http://vocabulary.odm2.org/medium/tissue', 'Tissue');
INSERT INTO cv_samplematerial VALUES (16, 'http://vocabulary.odm2.org/medium/unknown', 'unknown');
INSERT INTO cv_samplematerial VALUES (17, 'http://www.opengis.net/def/nil/OGC/0/inapplicable', 'inapplicable');
INSERT INTO cv_samplematerial VALUES (18, 'http://www.opengis.net/def/nil/OGC/0/missing', 'missing');
INSERT INTO cv_samplematerial VALUES (19, 'http://www.opengis.net/def/nil/OGC/0/template', 'template');
INSERT INTO cv_samplematerial VALUES (20, 'http://www.opengis.net/def/nil/OGC/0/unknown', 'unknown');
INSERT INTO cv_samplematerial VALUES (21, 'http://www.opengis.net/def/nil/OGC/0/withheld', 'withheld');


--
-- TOC entry 3843 (class 0 OID 0)
-- Dependencies: 249
-- Name: cv_samplematerial_materialid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_samplematerial_materialid_seq', 21, true);


--
-- TOC entry 3790 (class 0 OID 34019)
-- Dependencies: 250
-- Data for Name: cv_sampletype; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_sampletype VALUES (1, 'http://vocabulary.odm2.org/specimentype/automated', 'Automated');
INSERT INTO cv_sampletype VALUES (2, 'http://vocabulary.odm2.org/specimentype/core', 'Core');
INSERT INTO cv_sampletype VALUES (3, 'http://vocabulary.odm2.org/specimentype/coreHalfRound', 'Core Half Round');
INSERT INTO cv_sampletype VALUES (4, 'http://vocabulary.odm2.org/specimentype/corePiece', 'Core Piece');
INSERT INTO cv_sampletype VALUES (5, 'http://vocabulary.odm2.org/specimentype/coreQuarterRound', 'Core Quater Round');
INSERT INTO cv_sampletype VALUES (6, 'http://vocabulary.odm2.org/specimentype/coreSection', 'Core Section');
INSERT INTO cv_sampletype VALUES (7, 'http://vocabulary.odm2.org/specimentype/coreSectionHalf', 'Core Section Half');
INSERT INTO cv_sampletype VALUES (8, 'http://vocabulary.odm2.org/specimentype/coreSub-Piece', 'Core Sub-Piece');
INSERT INTO cv_sampletype VALUES (9, 'http://vocabulary.odm2.org/specimentype/coreWholeRound', 'Core Whole Round');
INSERT INTO cv_sampletype VALUES (10, 'http://vocabulary.odm2.org/specimentype/cuttings', 'Cutting');
INSERT INTO cv_sampletype VALUES (11, 'http://vocabulary.odm2.org/specimentype/dredge', 'Dredge');
INSERT INTO cv_sampletype VALUES (12, 'http://vocabulary.odm2.org/specimentype/foliageDigestion', 'Foliage Digestion');
INSERT INTO cv_sampletype VALUES (13, 'http://vocabulary.odm2.org/specimentype/foliageLeaching', 'Foliage Leaching');
INSERT INTO cv_sampletype VALUES (14, 'http://vocabulary.odm2.org/specimentype/forestFloorDigestion', 'Forest Floor Digestion');
INSERT INTO cv_sampletype VALUES (15, 'http://vocabulary.odm2.org/specimentype/individualSample', 'Individual Sample');
INSERT INTO cv_sampletype VALUES (16, 'http://vocabulary.odm2.org/specimentype/litterFallDigestion', 'Litter Fall Digestion');
INSERT INTO cv_sampletype VALUES (17, 'http://vocabulary.odm2.org/specimentype/other', 'Others');
INSERT INTO cv_sampletype VALUES (18, 'http://vocabulary.odm2.org/specimentype/petriDishDryDeposition', 'Petri Dish Dry Deposition');
INSERT INTO cv_sampletype VALUES (38, 'http://vocabulary.odm2.org/specimentype/precipitationBulk', 'Precipitation Bulk');
INSERT INTO cv_sampletype VALUES (39, 'http://vocabulary.odm2.org/specimentype/rockPowder', 'Rock Powder');
INSERT INTO cv_sampletype VALUES (40, 'http://vocabulary.odm2.org/specimentype/standardReferenceSpecimen', 'Standard Reference Specimen');
INSERT INTO cv_sampletype VALUES (41, 'http://vocabulary.odm2.org/specimentype/terrestrialSection', 'Terrestrial Section');
INSERT INTO cv_sampletype VALUES (42, 'http://vocabulary.odm2.org/specimentype/theSpecimenTypeIsUnknown', 'Specimen Type Unknown');
INSERT INTO cv_sampletype VALUES (43, 'http://vocabulary.odm2.org/specimentype/thinSection', 'Thin Section');
INSERT INTO cv_sampletype VALUES (44, 'http://www.opengis.net/def/nil/OGC/0/inapplicable', 'inapplicable');
INSERT INTO cv_sampletype VALUES (45, 'http://www.opengis.net/def/nil/OGC/0/missing', 'missing');
INSERT INTO cv_sampletype VALUES (46, 'http://www.opengis.net/def/nil/OGC/0/template', 'template');
INSERT INTO cv_sampletype VALUES (47, 'http://www.opengis.net/def/nil/OGC/0/unknown', 'unknown');
INSERT INTO cv_sampletype VALUES (48, 'http://www.opengis.net/def/nil/OGC/0/withheld', 'withheld');


--
-- TOC entry 3844 (class 0 OID 0)
-- Dependencies: 251
-- Name: cv_sampletype_sampletypeid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_sampletype_sampletypeid_seq', 48, true);


--
-- TOC entry 3792 (class 0 OID 34027)
-- Dependencies: 252
-- Data for Name: cv_samplingfeature; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_samplingfeature VALUES (67, 'Borehole', 'Borehole', 'http://vocabulary.odm2.org/samplingfeaturetype/borehole/');
INSERT INTO cv_samplingfeature VALUES (68, 'Cross Section', 'Cross Section', 'http://vocabulary.odm2.org/samplingfeaturetype/crossSection/');
INSERT INTO cv_samplingfeature VALUES (69, 'CTD', 'CTD', 'http://vocabulary.odm2.org/samplingfeaturetype/CTD/');
INSERT INTO cv_samplingfeature VALUES (70, 'Excavation', 'Excavation', 'http://vocabulary.odm2.org/samplingfeaturetype/excavation/');
INSERT INTO cv_samplingfeature VALUES (71, 'Field Area', 'Field Area', 'http://vocabulary.odm2.org/samplingfeaturetype/fieldArea/');
INSERT INTO cv_samplingfeature VALUES (72, 'Observation Well', 'Observation Well', 'http://vocabulary.odm2.org/samplingfeaturetype/observationWell/');
INSERT INTO cv_samplingfeature VALUES (73, 'Quadrat', 'Quadrat', 'http://vocabulary.odm2.org/samplingfeaturetype/quadrat/');
INSERT INTO cv_samplingfeature VALUES (74, 'Scene', 'Scene', 'http://vocabulary.odm2.org/samplingfeaturetype/scene/');
INSERT INTO cv_samplingfeature VALUES (75, 'Soil Pit Section', 'Soil Pit Section', 'http://vocabulary.odm2.org/samplingfeaturetype/soilPitSection/');


--
-- TOC entry 3845 (class 0 OID 0)
-- Dependencies: 253
-- Name: cv_samplingfeature_featureid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_samplingfeature_featureid_seq', 75, true);


--
-- TOC entry 3794 (class 0 OID 34035)
-- Dependencies: 254
-- Data for Name: cv_samplingmethod; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO cv_samplingmethod VALUES (1, 'http://www.opengis.net/def/nil/OGC/0/inapplicable', 'inapplicable');
INSERT INTO cv_samplingmethod VALUES (2, 'http://www.opengis.net/def/nil/OGC/0/missing', 'missing');
INSERT INTO cv_samplingmethod VALUES (3, 'http://www.opengis.net/def/nil/OGC/0/template', 'template');
INSERT INTO cv_samplingmethod VALUES (4, 'http://www.opengis.net/def/nil/OGC/0/unknown', 'unknown');
INSERT INTO cv_samplingmethod VALUES (5, 'http://www.opengis.net/def/nil/OGC/0/withheld', 'withheld');


--
-- TOC entry 3846 (class 0 OID 0)
-- Dependencies: 255
-- Name: cv_samplingmethod_methodid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('cv_samplingmethod_methodid_seq', 5, true);


--
-- TOC entry 3796 (class 0 OID 34043)
-- Dependencies: 256
-- Data for Name: prefix; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO prefix VALUES (12, 'CSCAP', '2015-10-28 11:32:16.316', 0);
INSERT INTO prefix VALUES (14, 'CS', '2015-12-01 17:16:14.706972', NULL);
INSERT INTO prefix VALUES (11, 'CSRWA', '2015-10-28 11:25:07.44', 0);
INSERT INTO prefix VALUES (10, 'CSTST', '2015-10-27 14:19:03.130218', 1);


--
-- TOC entry 3847 (class 0 OID 0)
-- Dependencies: 257
-- Name: prefix_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('prefix_id_seq', 15, true);


--
-- TOC entry 3798 (class 0 OID 34048)
-- Dependencies: 258
-- Data for Name: registrant; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO registrant VALUES (3, 'capricon', 'Pavel.Golodoniuc@csiro.au', '2015-07-01 00:00:00', 'capri', '1234', NULL, 7, NULL);
INSERT INTO registrant VALUES (5, 'rockstore', 'Victor.tey@csiro.au', '2015-10-27 14:18:02.32004', 'sa-rockstore', '1234', NULL, 7, NULL);
INSERT INTO registrant VALUES (12, 'victor', 'victor.tey@csiro.au', '2015-12-22 14:01:44.907572', 'tey006', '1234', NULL, 7, NULL);


--
-- TOC entry 3799 (class 0 OID 34054)
-- Dependencies: 259
-- Data for Name: registrant_prefixes; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO registrant_prefixes VALUES (5, 10);
INSERT INTO registrant_prefixes VALUES (5, 11);
INSERT INTO registrant_prefixes VALUES (3, 12);


--
-- TOC entry 3848 (class 0 OID 0)
-- Dependencies: 260
-- Name: registrant_registrantid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('registrant_registrantid_seq', 12, true);


--
-- TOC entry 3801 (class 0 OID 34059)
-- Dependencies: 261
-- Data for Name: sample; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3802 (class 0 OID 34065)
-- Dependencies: 262
-- Data for Name: sample_collector; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3849 (class 0 OID 0)
-- Dependencies: 263
-- Name: sample_collector_collectorid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('sample_collector_collectorid_seq', 1, true);


--
-- TOC entry 3804 (class 0 OID 34073)
-- Dependencies: 264
-- Data for Name: sample_features_mapping; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3850 (class 0 OID 0)
-- Dependencies: 265
-- Name: sample_features_mapping_objectid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('sample_features_mapping_objectid_seq', 1, true);


--
-- TOC entry 3806 (class 0 OID 34078)
-- Dependencies: 266
-- Data for Name: sample_material; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3851 (class 0 OID 0)
-- Dependencies: 267
-- Name: sample_sampleid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('sample_sampleid_seq', 1, true);


--
-- TOC entry 3808 (class 0 OID 34083)
-- Dependencies: 268
-- Data for Name: sample_types; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3809 (class 0 OID 34086)
-- Dependencies: 269
-- Data for Name: samplecuration; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3852 (class 0 OID 0)
-- Dependencies: 270
-- Name: samplecuration_samplecurationid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('samplecuration_samplecurationid_seq', 1, true);


--
-- TOC entry 3812 (class 0 OID 34096)
-- Dependencies: 272
-- Data for Name: sampledfeatures; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3853 (class 0 OID 0)
-- Dependencies: 271
-- Name: sampledfeatures_featureid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('sampledfeatures_featureid_seq', 1, true);


--
-- TOC entry 3813 (class 0 OID 34100)
-- Dependencies: 273
-- Data for Name: sampleresources; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3854 (class 0 OID 0)
-- Dependencies: 274
-- Name: sampleresources_sampleresourcesid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('sampleresources_sampleresourcesid_seq', 1, true);


--
-- TOC entry 3815 (class 0 OID 34105)
-- Dependencies: 275
-- Data for Name: sampling_sampled_mapping; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3816 (class 0 OID 34108)
-- Dependencies: 276
-- Data for Name: samplingfeatures; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3855 (class 0 OID 0)
-- Dependencies: 277
-- Name: samplingfeatures_featureid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('samplingfeatures_featureid_seq', 1, true);


--
-- TOC entry 3551 (class 0 OID 27329)
-- Dependencies: 174
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3818 (class 0 OID 34116)
-- Dependencies: 278
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO status VALUES (1, 'Destroyed', 'Physical sample', 'T physical sample has been destroyed');
INSERT INTO status VALUES (2, 'Submitted', 'Identifier', 'Tate of the initial IGSN registration');
INSERT INTO status VALUES (3, 'Registered', 'Identifier', 'The IGSN is registered');
INSERT INTO status VALUES (6, 'Updated', 'Sample Metadata', 'The sample metadata has been updated');
INSERT INTO status VALUES (7, 'Deprecated', 'Identifier', 'The IGSN is deprecated or no longer relevant, e.g., due to duplicate registration');


--
-- TOC entry 3856 (class 0 OID 0)
-- Dependencies: 279
-- Name: status_statusid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('status_statusid_seq', 7, true);


--
-- TOC entry 3573 (class 2606 OID 34138)
-- Name: allocator_prefixes_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT allocator_prefixes_primary PRIMARY KEY (allocator, prefixes);


--
-- TOC entry 3571 (class 2606 OID 34140)
-- Name: allocator_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator
    ADD CONSTRAINT allocator_primary PRIMARY KEY (allocatorid);


--
-- TOC entry 3576 (class 2606 OID 34142)
-- Name: cv_related_identifiertype_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_related_identifiertype
    ADD CONSTRAINT cv_related_identifiertype_primary PRIMARY KEY (objectid);


--
-- TOC entry 3580 (class 2606 OID 34144)
-- Name: cv_samplematerial_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplematerial
    ADD CONSTRAINT cv_samplematerial_primary PRIMARY KEY (materialid);


--
-- TOC entry 3582 (class 2606 OID 34146)
-- Name: cv_sampletype_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_sampletype
    ADD CONSTRAINT cv_sampletype_primary PRIMARY KEY (sampletypeid);


--
-- TOC entry 3588 (class 2606 OID 34148)
-- Name: cv_samplingfeature_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingfeature
    ADD CONSTRAINT cv_samplingfeature_primary PRIMARY KEY (featureid);


--
-- TOC entry 3590 (class 2606 OID 34150)
-- Name: cv_samplingmethod_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_samplingmethod
    ADD CONSTRAINT cv_samplingmethod_primary PRIMARY KEY (methodid);


--
-- TOC entry 3578 (class 2606 OID 34152)
-- Name: pk_cv_resource_relationship_type_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_resource_relationshiptype
    ADD CONSTRAINT pk_cv_resource_relationship_type_id PRIMARY KEY (id);


--
-- TOC entry 3595 (class 2606 OID 34154)
-- Name: pk_registrant_registrantid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT pk_registrant_registrantid PRIMARY KEY (registrantid);


--
-- TOC entry 3611 (class 2606 OID 34156)
-- Name: pk_sample_sampleid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT pk_sample_sampleid PRIMARY KEY (sampleid);


--
-- TOC entry 3632 (class 2606 OID 34158)
-- Name: pk_sampledfeatures_featureid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampledfeatures
    ADD CONSTRAINT pk_sampledfeatures_featureid PRIMARY KEY (featureid);


--
-- TOC entry 3592 (class 2606 OID 34160)
-- Name: prefix_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY prefix
    ADD CONSTRAINT prefix_primary PRIMARY KEY (id);


--
-- TOC entry 3604 (class 2606 OID 34162)
-- Name: registrant_prefixes_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT registrant_prefixes_primary PRIMARY KEY (registrant, prefixes);


--
-- TOC entry 3617 (class 2606 OID 34164)
-- Name: sample_collector_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_collector
    ADD CONSTRAINT sample_collector_primary PRIMARY KEY (collectorid);


--
-- TOC entry 3621 (class 2606 OID 34166)
-- Name: sample_features_mapping_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT sample_features_mapping_primary PRIMARY KEY (objectid);


--
-- TOC entry 3624 (class 2606 OID 34168)
-- Name: sample_material_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_material
    ADD CONSTRAINT sample_material_primary PRIMARY KEY (sampleid, materialid);


--
-- TOC entry 3627 (class 2606 OID 34170)
-- Name: sample_types_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_types
    ADD CONSTRAINT sample_types_primary PRIMARY KEY (sampleid, sampletypeid);


--
-- TOC entry 3630 (class 2606 OID 34172)
-- Name: samplecuration_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecuration
    ADD CONSTRAINT samplecuration_primary PRIMARY KEY (samplecurationid);


--
-- TOC entry 3636 (class 2606 OID 34174)
-- Name: sampleresources_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT sampleresources_primary PRIMARY KEY (sampleresourcesid);


--
-- TOC entry 3640 (class 2606 OID 34176)
-- Name: samplingfeatures_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures
    ADD CONSTRAINT samplingfeatures_primary PRIMARY KEY (featureid);


--
-- TOC entry 3642 (class 2606 OID 34178)
-- Name: status_primary; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY status
    ADD CONSTRAINT status_primary PRIMARY KEY (statusid);


--
-- TOC entry 3585 (class 2606 OID 34180)
-- Name: uk_1okumlu40xm0wh2ln7j0d6cti; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv_sampletype
    ADD CONSTRAINT uk_1okumlu40xm0wh2ln7j0d6cti UNIQUE (sampletypeidentifier);


--
-- TOC entry 3597 (class 2606 OID 34182)
-- Name: uk_on19yvmjqgud1jp4f251gajwy; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT uk_on19yvmjqgud1jp4f251gajwy UNIQUE (username);


--
-- TOC entry 3599 (class 2606 OID 34184)
-- Name: unique_registrant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT unique_registrant UNIQUE (registrantid);


--
-- TOC entry 3614 (class 2606 OID 34186)
-- Name: unique_samples_igsn; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT unique_samples_igsn UNIQUE (igsn);


--
-- TOC entry 3574 (class 1259 OID 34187)
-- Name: fk_ap_prefix_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ap_prefix_idx ON allocator_prefixes USING btree (prefixes);


--
-- TOC entry 3615 (class 1259 OID 34188)
-- Name: fk_collector_sample_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_collector_sample_idx ON sample_collector USING btree (sampleid);


--
-- TOC entry 3628 (class 1259 OID 34189)
-- Name: fk_curation_sample_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_curation_sample_idx ON samplecuration USING btree (sampleid);


--
-- TOC entry 3637 (class 1259 OID 34190)
-- Name: fk_feature_srs_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_feature_srs_idx ON samplingfeatures USING btree (featuresrs);


--
-- TOC entry 3618 (class 1259 OID 34191)
-- Name: fk_feature_tosample_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_feature_tosample_idx ON sample_features_mapping USING btree (featureid);


--
-- TOC entry 3638 (class 1259 OID 34192)
-- Name: fk_feature_type_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_feature_type_idx ON samplingfeatures USING btree (featuretype);


--
-- TOC entry 3625 (class 1259 OID 34193)
-- Name: fk_mapping_sampletype_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_mapping_sampletype_idx ON sample_types USING btree (sampletypeid);


--
-- TOC entry 3605 (class 1259 OID 34194)
-- Name: fk_metadatastatus_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_metadatastatus_status_idx ON sample USING btree (registrationstatus);


--
-- TOC entry 3606 (class 1259 OID 34195)
-- Name: fk_physical_samplestatus_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_physical_samplestatus_idx ON sample USING btree (physicalsamplestatus);


--
-- TOC entry 3593 (class 1259 OID 34196)
-- Name: fk_registrant_alloc_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_registrant_alloc_idx ON registrant USING btree (allocator);


--
-- TOC entry 3633 (class 1259 OID 34197)
-- Name: fk_resource_identtype_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_resource_identtype_idx ON sampleresources USING btree (resourceidentifertype);


--
-- TOC entry 3634 (class 1259 OID 34198)
-- Name: fk_resources_sample_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_resources_sample_idx ON sampleresources USING btree (sampleid);


--
-- TOC entry 3601 (class 1259 OID 34199)
-- Name: fk_rp_prefix_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_rp_prefix_idx ON registrant_prefixes USING btree (prefixes);


--
-- TOC entry 3607 (class 1259 OID 34200)
-- Name: fk_sample_classification_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sample_classification_idx ON sample USING btree (classification);


--
-- TOC entry 3608 (class 1259 OID 34201)
-- Name: fk_sample_method_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sample_method_idx ON sample USING btree (samplingmethod);


--
-- TOC entry 3619 (class 1259 OID 34202)
-- Name: fk_sample_tofeature_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sample_tofeature_idx ON sample_features_mapping USING btree (sampleid);


--
-- TOC entry 3622 (class 1259 OID 34203)
-- Name: fk_sm_material_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sm_material_idx ON sample_material USING btree (materialid);


--
-- TOC entry 3602 (class 1259 OID 34204)
-- Name: idx_registrant_prefixes; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_registrant_prefixes ON registrant_prefixes USING btree (registrant);


--
-- TOC entry 3609 (class 1259 OID 34205)
-- Name: igsn_unique; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX igsn_unique ON sample USING btree (igsn);


--
-- TOC entry 3612 (class 1259 OID 34206)
-- Name: sampleid_unique; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX sampleid_unique ON sample USING btree (sampleid);


--
-- TOC entry 3583 (class 1259 OID 34207)
-- Name: term_unique; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX term_unique ON cv_sampletype USING btree (sampletypeidentifier);


--
-- TOC entry 3586 (class 1259 OID 34208)
-- Name: uk_6y9um0475rj9vd5v07isob9ct; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_6y9um0475rj9vd5v07isob9ct ON cv_sampletype USING btree (sampletypeidentifier);


--
-- TOC entry 3600 (class 1259 OID 34209)
-- Name: username_unique; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX username_unique ON registrant USING btree (username);


--
-- TOC entry 3644 (class 2606 OID 34210)
-- Name: fk_ap_allocator; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT fk_ap_allocator FOREIGN KEY (allocator) REFERENCES allocator(allocatorid);


--
-- TOC entry 3643 (class 2606 OID 34215)
-- Name: fk_ap_prefix; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY allocator_prefixes
    ADD CONSTRAINT fk_ap_prefix FOREIGN KEY (prefixes) REFERENCES prefix(id);


--
-- TOC entry 3652 (class 2606 OID 34220)
-- Name: fk_collector_sample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_collector
    ADD CONSTRAINT fk_collector_sample FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


--
-- TOC entry 3659 (class 2606 OID 34225)
-- Name: fk_curator_sample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplecuration
    ADD CONSTRAINT fk_curator_sample FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


--
-- TOC entry 3654 (class 2606 OID 34230)
-- Name: fk_feature_tosample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT fk_feature_tosample FOREIGN KEY (featureid) REFERENCES samplingfeatures(featureid);


--
-- TOC entry 3666 (class 2606 OID 34235)
-- Name: fk_feature_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY samplingfeatures
    ADD CONSTRAINT fk_feature_type FOREIGN KEY (featuretype) REFERENCES cv_samplingfeature(featureid);


--
-- TOC entry 3665 (class 2606 OID 34240)
-- Name: fk_ft4tgm0x2o8jw6w1wp8tyb2n3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampling_sampled_mapping
    ADD CONSTRAINT fk_ft4tgm0x2o8jw6w1wp8tyb2n3 FOREIGN KEY (sampledfeature_id) REFERENCES samplingfeatures(featureid);


--
-- TOC entry 3658 (class 2606 OID 34245)
-- Name: fk_mapping_sample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_types
    ADD CONSTRAINT fk_mapping_sample FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


--
-- TOC entry 3657 (class 2606 OID 34250)
-- Name: fk_mapping_sampletype; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_types
    ADD CONSTRAINT fk_mapping_sampletype FOREIGN KEY (sampletypeid) REFERENCES cv_sampletype(sampletypeid);


--
-- TOC entry 3651 (class 2606 OID 34255)
-- Name: fk_metadatastatus_status; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_metadatastatus_status FOREIGN KEY (registrationstatus) REFERENCES status(statusid);


--
-- TOC entry 3650 (class 2606 OID 34260)
-- Name: fk_physical_samplestatus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_physical_samplestatus FOREIGN KEY (physicalsamplestatus) REFERENCES status(statusid);


--
-- TOC entry 3645 (class 2606 OID 34265)
-- Name: fk_registrant_alloc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant
    ADD CONSTRAINT fk_registrant_alloc FOREIGN KEY (allocator) REFERENCES allocator(allocatorid);


--
-- TOC entry 3647 (class 2606 OID 34270)
-- Name: fk_registrant_prefixes; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT fk_registrant_prefixes FOREIGN KEY (registrant) REFERENCES registrant(registrantid);


--
-- TOC entry 3662 (class 2606 OID 34275)
-- Name: fk_resource_identtype; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT fk_resource_identtype FOREIGN KEY (resourceidentifertype) REFERENCES cv_related_identifiertype(objectid);


--
-- TOC entry 3661 (class 2606 OID 34280)
-- Name: fk_resource_sample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT fk_resource_sample FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


--
-- TOC entry 3660 (class 2606 OID 34285)
-- Name: fk_resourcerelationtype; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampleresources
    ADD CONSTRAINT fk_resourcerelationtype FOREIGN KEY (resourcerelationtype) REFERENCES cv_resource_relationshiptype(id);


--
-- TOC entry 3646 (class 2606 OID 34290)
-- Name: fk_rp_prefix; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registrant_prefixes
    ADD CONSTRAINT fk_rp_prefix FOREIGN KEY (prefixes) REFERENCES prefix(id);


--
-- TOC entry 3649 (class 2606 OID 34295)
-- Name: fk_sample_method; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_method FOREIGN KEY (samplingmethod) REFERENCES cv_samplingmethod(methodid);


--
-- TOC entry 3648 (class 2606 OID 34300)
-- Name: fk_sample_registrant; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT fk_sample_registrant FOREIGN KEY (registrant) REFERENCES registrant(registrantid);


--
-- TOC entry 3653 (class 2606 OID 34305)
-- Name: fk_sample_tofeature; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_features_mapping
    ADD CONSTRAINT fk_sample_tofeature FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


--
-- TOC entry 3664 (class 2606 OID 34310)
-- Name: fk_samplling_sampled_mapping_sampledfeature; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampling_sampled_mapping
    ADD CONSTRAINT fk_samplling_sampled_mapping_sampledfeature FOREIGN KEY (sampledfeature_id) REFERENCES sampledfeatures(featureid);


--
-- TOC entry 3663 (class 2606 OID 34315)
-- Name: fk_samplling_sampled_mapping_samplingfeature; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sampling_sampled_mapping
    ADD CONSTRAINT fk_samplling_sampled_mapping_samplingfeature FOREIGN KEY (samplingfeature_id) REFERENCES samplingfeatures(featureid);


--
-- TOC entry 3656 (class 2606 OID 34320)
-- Name: fk_smat_materials; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_material
    ADD CONSTRAINT fk_smat_materials FOREIGN KEY (materialid) REFERENCES cv_samplematerial(materialid);


--
-- TOC entry 3655 (class 2606 OID 34325)
-- Name: fk_smat_sample; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sample_material
    ADD CONSTRAINT fk_smat_sample FOREIGN KEY (sampleid) REFERENCES sample(sampleid);


-- Completed on 2015-12-22 11:14:39

--
-- PostgreSQL database dump complete
--

