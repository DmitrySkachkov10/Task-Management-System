--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.4

-- Started on 2024-08-08 13:58:39 +03

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
-- TOC entry 6 (class 2615 OID 88068)
-- Name: user_management; Type: SCHEMA; Schema: -; Owner: dmitry
--

CREATE SCHEMA user_management;


ALTER SCHEMA user_management OWNER TO dmitry;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 88141)
-- Name: users; Type: TABLE; Schema: user_management; Owner: dmitry
--

CREATE TABLE user_management.users (
    uuid uuid NOT NULL,
    email character varying(255),
    password character varying(255)
);


ALTER TABLE user_management.users OWNER TO dmitry;

--
-- TOC entry 3440 (class 2606 OID 88149)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: user_management; Owner: dmitry
--

ALTER TABLE ONLY user_management.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3442 (class 2606 OID 88147)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: user_management; Owner: dmitry
--

ALTER TABLE ONLY user_management.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (uuid);


-- Completed on 2024-08-08 13:58:40 +03

--
-- PostgreSQL database dump complete
--

