import { createClient } from '@supabase/supabase-js';

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL; // Replace with your Supabase URL
const supabaseKey = process.env.NEXT_PUBLIC_SUPABASE_KEY; // Replace with your Supabase Anon Key

const supabase = createClient(supabaseUrl, supabaseKey);

export default supabase;
