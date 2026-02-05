--
-- PostgreSQL database dump
--

\restrict Z1nXokOxds0wJRuLcGKd0FcPTTFUKLcPKwZlSvTxhUCx1UJRbeVQGKBQGhafXhv

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-01-09 02:54:17 MSK

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 230 (class 1259 OID 16481)
-- Name: favorites; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.favorites (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    roadmap_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.favorites OWNER TO deart;

--
-- TOC entry 229 (class 1259 OID 16480)
-- Name: favorites_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.favorites_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.favorites_id_seq OWNER TO deart;

--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 229
-- Name: favorites_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.favorites_id_seq OWNED BY public.favorites.id;


--
-- TOC entry 226 (class 1259 OID 16444)
-- Name: roadmap_steps; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.roadmap_steps (
    id bigint NOT NULL,
    roadmap_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    image_url text,
    order_index integer NOT NULL
);


ALTER TABLE public.roadmap_steps OWNER TO deart;

--
-- TOC entry 225 (class 1259 OID 16443)
-- Name: roadmap_steps_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.roadmap_steps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roadmap_steps_id_seq OWNER TO deart;

--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 225
-- Name: roadmap_steps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.roadmap_steps_id_seq OWNED BY public.roadmap_steps.id;


--
-- TOC entry 224 (class 1259 OID 16425)
-- Name: roadmaps; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.roadmaps (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    image_url text,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.roadmaps OWNER TO deart;

--
-- TOC entry 223 (class 1259 OID 16424)
-- Name: roadmaps_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.roadmaps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roadmaps_id_seq OWNER TO deart;

--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 223
-- Name: roadmaps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.roadmaps_id_seq OWNED BY public.roadmaps.id;


--
-- TOC entry 228 (class 1259 OID 16463)
-- Name: step_links; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.step_links (
    id bigint NOT NULL,
    step_id bigint NOT NULL,
    title character varying(255),
    url text NOT NULL
);


ALTER TABLE public.step_links OWNER TO deart;

--
-- TOC entry 227 (class 1259 OID 16462)
-- Name: step_links_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.step_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.step_links_id_seq OWNER TO deart;

--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 227
-- Name: step_links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.step_links_id_seq OWNED BY public.step_links.id;


--
-- TOC entry 222 (class 1259 OID 16407)
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.user_profiles (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    name character varying(255),
    about text,
    avatar_url text
);


ALTER TABLE public.user_profiles OWNER TO deart;

--
-- TOC entry 221 (class 1259 OID 16406)
-- Name: user_profiles_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.user_profiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_profiles_id_seq OWNER TO deart;

--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_profiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.user_profiles_id_seq OWNED BY public.user_profiles.id;


--
-- TOC entry 232 (class 1259 OID 16506)
-- Name: user_roadmap_progress; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.user_roadmap_progress (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    roadmap_id bigint NOT NULL,
    progress_percent integer NOT NULL,
    last_step_id bigint,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT user_roadmap_progress_progress_percent_check CHECK (((progress_percent >= 0) AND (progress_percent <= 100)))
);


ALTER TABLE public.user_roadmap_progress OWNER TO deart;

--
-- TOC entry 231 (class 1259 OID 16505)
-- Name: user_roadmap_progress_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.user_roadmap_progress_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_roadmap_progress_id_seq OWNER TO deart;

--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 231
-- Name: user_roadmap_progress_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.user_roadmap_progress_id_seq OWNED BY public.user_roadmap_progress.id;


--
-- TOC entry 234 (class 1259 OID 16538)
-- Name: user_step_progress; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.user_step_progress (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    step_id bigint NOT NULL,
    is_completed boolean DEFAULT false NOT NULL,
    completed_at timestamp without time zone
);


ALTER TABLE public.user_step_progress OWNER TO deart;

--
-- TOC entry 233 (class 1259 OID 16537)
-- Name: user_step_progress_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.user_step_progress_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_step_progress_id_seq OWNER TO deart;

--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 233
-- Name: user_step_progress_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.user_step_progress_id_seq OWNED BY public.user_step_progress.id;


--
-- TOC entry 220 (class 1259 OID 16391)
-- Name: users; Type: TABLE; Schema: public; Owner: deart
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    password_hash text NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.users OWNER TO deart;

--
-- TOC entry 219 (class 1259 OID 16390)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: deart
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO deart;

--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: deart
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3368 (class 2604 OID 16484)
-- Name: favorites id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.favorites ALTER COLUMN id SET DEFAULT nextval('public.favorites_id_seq'::regclass);


--
-- TOC entry 3366 (class 2604 OID 16447)
-- Name: roadmap_steps id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.roadmap_steps ALTER COLUMN id SET DEFAULT nextval('public.roadmap_steps_id_seq'::regclass);


--
-- TOC entry 3364 (class 2604 OID 16428)
-- Name: roadmaps id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.roadmaps ALTER COLUMN id SET DEFAULT nextval('public.roadmaps_id_seq'::regclass);


--
-- TOC entry 3367 (class 2604 OID 16466)
-- Name: step_links id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.step_links ALTER COLUMN id SET DEFAULT nextval('public.step_links_id_seq'::regclass);


--
-- TOC entry 3363 (class 2604 OID 16410)
-- Name: user_profiles id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_profiles ALTER COLUMN id SET DEFAULT nextval('public.user_profiles_id_seq'::regclass);


--
-- TOC entry 3370 (class 2604 OID 16509)
-- Name: user_roadmap_progress id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress ALTER COLUMN id SET DEFAULT nextval('public.user_roadmap_progress_id_seq'::regclass);


--
-- TOC entry 3372 (class 2604 OID 16541)
-- Name: user_step_progress id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_step_progress ALTER COLUMN id SET DEFAULT nextval('public.user_step_progress_id_seq'::regclass);


--
-- TOC entry 3361 (class 2604 OID 16394)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3576 (class 0 OID 16481)
-- Dependencies: 230
-- Data for Name: favorites; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.favorites (id, user_id, roadmap_id, created_at) FROM stdin;
\.


--
-- TOC entry 3572 (class 0 OID 16444)
-- Dependencies: 226
-- Data for Name: roadmap_steps; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.roadmap_steps (id, roadmap_id, title, description, image_url, order_index) FROM stdin;
\.


--
-- TOC entry 3570 (class 0 OID 16425)
-- Dependencies: 224
-- Data for Name: roadmaps; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.roadmaps (id, title, description, image_url, created_at) FROM stdin;
\.


--
-- TOC entry 3574 (class 0 OID 16463)
-- Dependencies: 228
-- Data for Name: step_links; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.step_links (id, step_id, title, url) FROM stdin;
\.


--
-- TOC entry 3568 (class 0 OID 16407)
-- Dependencies: 222
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.user_profiles (id, user_id, name, about, avatar_url) FROM stdin;
\.


--
-- TOC entry 3578 (class 0 OID 16506)
-- Dependencies: 232
-- Data for Name: user_roadmap_progress; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.user_roadmap_progress (id, user_id, roadmap_id, progress_percent, last_step_id, updated_at) FROM stdin;
\.


--
-- TOC entry 3580 (class 0 OID 16538)
-- Dependencies: 234
-- Data for Name: user_step_progress; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.user_step_progress (id, user_id, step_id, is_completed, completed_at) FROM stdin;
\.


--
-- TOC entry 3566 (class 0 OID 16391)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: deart
--

COPY public.users (id, email, password_hash, created_at) FROM stdin;
\.


--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 229
-- Name: favorites_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.favorites_id_seq', 1, false);


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 225
-- Name: roadmap_steps_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.roadmap_steps_id_seq', 1, false);


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 223
-- Name: roadmaps_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.roadmaps_id_seq', 1, false);


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 227
-- Name: step_links_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.step_links_id_seq', 1, false);


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.user_profiles_id_seq', 1, false);


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 231
-- Name: user_roadmap_progress_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.user_roadmap_progress_id_seq', 1, false);


--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 233
-- Name: user_step_progress_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.user_step_progress_id_seq', 1, false);


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: deart
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- TOC entry 3393 (class 2606 OID 16491)
-- Name: favorites favorites_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_pkey PRIMARY KEY (id);


--
-- TOC entry 3388 (class 2606 OID 16455)
-- Name: roadmap_steps roadmap_steps_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.roadmap_steps
    ADD CONSTRAINT roadmap_steps_pkey PRIMARY KEY (id);


--
-- TOC entry 3385 (class 2606 OID 16436)
-- Name: roadmaps roadmaps_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.roadmaps
    ADD CONSTRAINT roadmaps_pkey PRIMARY KEY (id);


--
-- TOC entry 3391 (class 2606 OID 16473)
-- Name: step_links step_links_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.step_links
    ADD CONSTRAINT step_links_pkey PRIMARY KEY (id);


--
-- TOC entry 3396 (class 2606 OID 16493)
-- Name: favorites uq_favorite; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT uq_favorite UNIQUE (user_id, roadmap_id);


--
-- TOC entry 3399 (class 2606 OID 16520)
-- Name: user_roadmap_progress uq_user_roadmap; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress
    ADD CONSTRAINT uq_user_roadmap UNIQUE (user_id, roadmap_id);


--
-- TOC entry 3404 (class 2606 OID 16550)
-- Name: user_step_progress uq_user_step; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_step_progress
    ADD CONSTRAINT uq_user_step UNIQUE (user_id, step_id);


--
-- TOC entry 3380 (class 2606 OID 16416)
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (id);


--
-- TOC entry 3382 (class 2606 OID 16418)
-- Name: user_profiles user_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_key UNIQUE (user_id);


--
-- TOC entry 3401 (class 2606 OID 16518)
-- Name: user_roadmap_progress user_roadmap_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress
    ADD CONSTRAINT user_roadmap_progress_pkey PRIMARY KEY (id);


--
-- TOC entry 3406 (class 2606 OID 16548)
-- Name: user_step_progress user_step_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_step_progress
    ADD CONSTRAINT user_step_progress_pkey PRIMARY KEY (id);


--
-- TOC entry 3376 (class 2606 OID 16405)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3378 (class 2606 OID 16403)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3394 (class 1259 OID 16504)
-- Name: idx_favorites_user; Type: INDEX; Schema: public; Owner: deart
--

CREATE INDEX idx_favorites_user ON public.favorites USING btree (user_id);


--
-- TOC entry 3389 (class 1259 OID 16479)
-- Name: idx_links_step; Type: INDEX; Schema: public; Owner: deart
--

CREATE INDEX idx_links_step ON public.step_links USING btree (step_id);


--
-- TOC entry 3397 (class 1259 OID 16536)
-- Name: idx_progress_user; Type: INDEX; Schema: public; Owner: deart
--

CREATE INDEX idx_progress_user ON public.user_roadmap_progress USING btree (user_id);


--
-- TOC entry 3402 (class 1259 OID 16561)
-- Name: idx_step_progress_user; Type: INDEX; Schema: public; Owner: deart
--

CREATE INDEX idx_step_progress_user ON public.user_step_progress USING btree (user_id);


--
-- TOC entry 3386 (class 1259 OID 16461)
-- Name: idx_steps_roadmap; Type: INDEX; Schema: public; Owner: deart
--

CREATE INDEX idx_steps_roadmap ON public.roadmap_steps USING btree (roadmap_id);


--
-- TOC entry 3411 (class 2606 OID 16499)
-- Name: favorites fk_favorite_roadmap; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT fk_favorite_roadmap FOREIGN KEY (roadmap_id) REFERENCES public.roadmaps(id) ON DELETE CASCADE;


--
-- TOC entry 3412 (class 2606 OID 16494)
-- Name: favorites fk_favorite_user; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3410 (class 2606 OID 16474)
-- Name: step_links fk_link_step; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.step_links
    ADD CONSTRAINT fk_link_step FOREIGN KEY (step_id) REFERENCES public.roadmap_steps(id) ON DELETE CASCADE;


--
-- TOC entry 3407 (class 2606 OID 16419)
-- Name: user_profiles fk_profile_user; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3413 (class 2606 OID 16526)
-- Name: user_roadmap_progress fk_progress_roadmap; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress
    ADD CONSTRAINT fk_progress_roadmap FOREIGN KEY (roadmap_id) REFERENCES public.roadmaps(id) ON DELETE CASCADE;


--
-- TOC entry 3414 (class 2606 OID 16531)
-- Name: user_roadmap_progress fk_progress_step; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress
    ADD CONSTRAINT fk_progress_step FOREIGN KEY (last_step_id) REFERENCES public.roadmap_steps(id) ON DELETE SET NULL;


--
-- TOC entry 3415 (class 2606 OID 16521)
-- Name: user_roadmap_progress fk_progress_user; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_roadmap_progress
    ADD CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3416 (class 2606 OID 16556)
-- Name: user_step_progress fk_step_progress_step; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_step_progress
    ADD CONSTRAINT fk_step_progress_step FOREIGN KEY (step_id) REFERENCES public.roadmap_steps(id) ON DELETE CASCADE;


--
-- TOC entry 3417 (class 2606 OID 16551)
-- Name: user_step_progress fk_step_progress_user; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.user_step_progress
    ADD CONSTRAINT fk_step_progress_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3409 (class 2606 OID 16456)
-- Name: roadmap_steps fk_step_roadmap; Type: FK CONSTRAINT; Schema: public; Owner: deart
--

ALTER TABLE ONLY public.roadmap_steps
    ADD CONSTRAINT fk_step_roadmap FOREIGN KEY (roadmap_id) REFERENCES public.roadmaps(id) ON DELETE CASCADE;


-- Completed on 2026-01-09 02:54:17 MSK

--
-- PostgreSQL database dump complete
--

\unrestrict Z1nXokOxds0wJRuLcGKd0FcPTTFUKLcPKwZlSvTxhUCx1UJRbeVQGKBQGhafXhv

