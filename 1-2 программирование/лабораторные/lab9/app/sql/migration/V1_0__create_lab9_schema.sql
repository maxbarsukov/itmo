--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 14.2 (Debian 14.2-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: organization_type; Type: TYPE; Schema: public; Owner: s367081
--

CREATE TYPE public.organization_type AS ENUM (
    'COMMERCIAL',
    'GOVERNMENT',
    'TRUST',
    'PRIVATE_LIMITED_COMPANY'
);


ALTER TYPE public.organization_type OWNER TO s367081;

--
-- Name: unit_of_measure; Type: TYPE; Schema: public; Owner: s367081
--

CREATE TYPE public.unit_of_measure AS ENUM (
    'KILOGRAMS',
    'SQUARE_METERS',
    'LITERS',
    'MILLILITERS'
);


ALTER TYPE public.unit_of_measure OWNER TO s367081;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: s367081
--

CREATE TABLE public.flyway_schema_history (
                                            installed_rank integer NOT NULL,
                                            version character varying(50),
                                            description character varying(200) NOT NULL,
                                            type character varying(20) NOT NULL,
                                            script character varying(1000) NOT NULL,
                                            checksum integer,
                                            installed_by character varying(100) NOT NULL,
                                            installed_on timestamp without time zone DEFAULT now() NOT NULL,
                                            execution_time integer NOT NULL,
                                            success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO s367081;

--
-- Name: organizations; Type: TABLE; Schema: public; Owner: s367081
--

CREATE TABLE public.organizations (
                                    id integer NOT NULL,
                                    employees_count bigint NOT NULL,
                                    name character varying(255) NOT NULL,
                                    street character varying(255) NOT NULL,
                                    type character varying(255) NOT NULL,
                                    zip_code character varying(255),
                                    creator_id integer NOT NULL
);


ALTER TABLE public.organizations OWNER TO s367081;

--
-- Name: organizations_id_seq; Type: SEQUENCE; Schema: public; Owner: s367081
--

CREATE SEQUENCE public.organizations_id_seq
  AS integer
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER TABLE public.organizations_id_seq OWNER TO s367081;

--
-- Name: organizations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: s367081
--

ALTER SEQUENCE public.organizations_id_seq OWNED BY public.organizations.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: s367081
--

CREATE TABLE public.products (
                               id integer NOT NULL,
                               creation_date date NOT NULL,
                               name character varying(255) NOT NULL,
                               part_number character varying(255),
                               price bigint NOT NULL,
                               unit_of_measure character varying(255),
                               x integer NOT NULL,
                               y bigint NOT NULL,
                               creator_id integer NOT NULL,
                               manufacturer_id integer
);


ALTER TABLE public.products OWNER TO s367081;

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: s367081
--

CREATE SEQUENCE public.products_id_seq
  AS integer
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER TABLE public.products_id_seq OWNER TO s367081;

--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: s367081
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: s367081
--

CREATE TABLE public.users (
                            id integer NOT NULL,
                            name character varying(40) NOT NULL,
                            password_digest character varying(64) NOT NULL,
                            salt character varying(10) NOT NULL
);


ALTER TABLE public.users OWNER TO s367081;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: s367081
--

CREATE SEQUENCE public.users_id_seq
  AS integer
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO s367081;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: s367081
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: organizations id; Type: DEFAULT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.organizations ALTER COLUMN id SET DEFAULT nextval('public.organizations_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1.0	create lab8 schema	SQL	V1_0__create_lab9_schema.sql	-2015935833	s367081	2023-04-20 23:07:51.0945	27	t
\.


--
-- Data for Name: organizations; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.organizations (id, employees_count, name, street, type, zip_code, creator_id) FROM stdin;
1	100	org	street	TRUST	123456	13
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.products (id, creation_date, name, part_number, price, unit_of_measure, x, y, creator_id, manufacturer_id) FROM stdin;
1	2023-03-29	123	12	1	\N	123	12	1	\N
2	2023-05-07	111	part-1	100	LITERS	1123	-11	13	1
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.users (id, name, password_digest, salt) FROM stdin;
1	a	193f50a746f1247db2800f2aa73894f9faaa6385337a238c8e0dbb39604993b0	PWmy3oKw05
11	Hello	266ffb5ab423671d5a3531a0d00e8e6fca1f9bc2de0a80724aa547bd81309e56	ELkAFMrYqY
12	Hello2	f17f4d543fbb3b654eec8720624f7abb4a9fa6ec68e8452f84085df4bda26a11	hyNXAlXvZz
13	Hello123	1674672e99a9ddad16f08a590b7e777d3cfbe88054a437fa3e1fb51c7eb9fb49	ZeCKl0VliI
\.


--
-- Name: organizations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.organizations_id_seq', 1, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.products_id_seq', 2, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.users_id_seq', 13, true);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.flyway_schema_history
  ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: organizations organizations_pkey; Type: CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.organizations
  ADD CONSTRAINT organizations_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.products
  ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: users uk_3g1j96g94xpk3lpxl2qbl985x; Type: CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.users
  ADD CONSTRAINT uk_3g1j96g94xpk3lpxl2qbl985x UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.users
  ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: s367081
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: products fkid1quonr11ajt1rq1xbvx9p47; Type: FK CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.products
  ADD CONSTRAINT fkid1quonr11ajt1rq1xbvx9p47 FOREIGN KEY (creator_id) REFERENCES public.users(id);


--
-- Name: organizations fkm1est60p6bxfm3jdira1o7j9c; Type: FK CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.organizations
  ADD CONSTRAINT fkm1est60p6bxfm3jdira1o7j9c FOREIGN KEY (creator_id) REFERENCES public.users(id);


--
-- Name: products fkosamd2d5l9l0x9j0spa4s649w; Type: FK CONSTRAINT; Schema: public; Owner: s367081
--

ALTER TABLE ONLY public.products
  ADD CONSTRAINT fkosamd2d5l9l0x9j0spa4s649w FOREIGN KEY (manufacturer_id) REFERENCES public.organizations(id);


--
-- PostgreSQL database dump complete
--

