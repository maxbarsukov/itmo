--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 14.5

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
-- Data for Name: organizations; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.organizations (id, employees_count, name, street, type, zip_code, creator_id) FROM stdin;
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.products (id, creation_date, name, part_number, price, unit_of_measure, x, y, creator_id, manufacturer_id) FROM stdin;
1	2023-03-29	123	12	1	\N	123	12	1	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: s367081
--

COPY public.users (id, name, password_digest, salt) FROM stdin;
1	a	193f50a746f1247db2800f2aa73894f9faaa6385337a238c8e0dbb39604993b0	PWmy3oKw05
\.


--
-- Name: organizations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.organizations_id_seq', 1, false);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.products_id_seq', 1, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s367081
--

SELECT pg_catalog.setval('public.users_id_seq', 1, true);


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

